package ru.otus.vsh.knb.webCore.gamePage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.domain.DefaultValues;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.webCore.GameDataKeeper;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.SessionKeeper;
import ru.otus.vsh.knb.webCore.gamePage.data.*;

@Controller
@AllArgsConstructor
@Slf4j
public class GamePageController {
    private static final String TEMPLATE_PLAYER_NAME = "playerName";
    private static final String GAME_HTML = "game.html";
    private static final String TEMPLATE_SESSION_ID = "sessionId";

    private final SimpMessagingTemplate template;
    private final SessionKeeper sessionKeeper;
    private final GameDataKeeper gameDataKeeper;
    private final GameControllerMSClient gameControllerMSClient;

    @GetMapping(Routes.GAME)
    public String getGamePage(Model model) {
        val sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        val loggedInPerson = sessionKeeper.get(sessionId).orElseThrow(() -> new GameException("Session id without a player"));

        model.addAttribute(TEMPLATE_PLAYER_NAME, loggedInPerson.getName());
        model.addAttribute(TEMPLATE_SESSION_ID, sessionId);
        return GAME_HTML;
    }

    @MessageMapping(Routes.API_GAME_HELLO)
    public void loadData(@DestinationVariable String sessionId) {
        log.warn("load data for sessionId {}", sessionId);
        val loggedInPerson = sessionKeeper.get(sessionId);
        if (loggedInPerson.isEmpty()) return;
        val gameData = gameDataKeeper.get(sessionId);
        if (gameData.isEmpty()) return;
        boolean isGameReady = false;
        boolean isPlayer = loggedInPerson.get().equals(gameData.get().getPlayer1()) ||
                (gameData.get().getPlayer2() != null && loggedInPerson.get().equals(gameData.get().getPlayer2()));
        val initialGameInfo = UIGameInitialInfo.builder()
                .gameId(gameData.get().getGame().getId())
                .playerName1(gameData.get().getPlayer1().getName())
                .isPlayer(String.valueOf(isPlayer))
                .money1(gameData.get().getPlayer1().getAccount().getSum())
                .turns(gameData.get().getGame().getSettings().getNumberOfTurns())
                .figures(UIFigure.from(gameData.get().getRule().getRange()))
                .cheats(gameData.get().getGame().getSettings().getNumberOfCheats())
                .bet(gameData.get().getWager());
        if (gameData.get().getPlayer2() != null) {
            isGameReady = true;
            initialGameInfo
                    .playerName2(gameData.get().getPlayer2().getName())
                    .money2(gameData.get().getPlayer2().getAccount().getSum());
        } else {
            initialGameInfo
                    .playerName2("???")
                    .money2(0);
        }

        if (isPlayer) {
            val playersInGame = gameDataKeeper.byGameId(gameData.get().getGame().getId());
            playersInGame.forEach(id -> template.convertAndSend(Routes.TOPIC_GAME_INFO + "." + id, initialGameInfo.get()));
            if (isGameReady) {
                val turnReady = UITurnReady.builder()
                        .turn(0)
                        .availCheats(initialGameInfo.get().getCheats())
                        .get();
                playersInGame.forEach(id -> template.convertAndSend(Routes.TOPIC_GAME_TURN_START + "." + id, turnReady));
            }
        } else {
            // Observer sends game info only to themself
            template.convertAndSend(Routes.TOPIC_GAME_INFO + "." + sessionId, initialGameInfo.get());
        }

    }

    @MessageMapping(Routes.API_GAME_TURN_END)
    public void processTurn(@DestinationVariable long gameId, UITurnEnd turnEnd) {
        log.warn("process turn for game {}, data {}", gameId, turnEnd);
        val gameData = gameDataKeeper.get(gameId);
        if (gameData.isEmpty()) throw new GameException(String.format("No game data for gameId %d", gameId));
        val turnData = gameDataKeeper.getTurnData(gameId);
        val person = sessionKeeper.get(turnEnd.getSessionId())
                .orElseThrow(() -> new GameException("Player participates without session id"));
        if (gameData.get().getPlayer1().getId() == person.getId()) {
            turnData.figure1(turnEnd.getFigure());
        } else {
            turnData.figure2(turnEnd.getFigure());
        }
        val currentTurn = turnData.currentTurn();
        log.warn("turn data {} before barrier", turnData);

        turnData.awaitBarrier();

        if (currentTurn == turnData.currentTurn()) {
            synchronized (this) {
                if (currentTurn == turnData.currentTurn()) {
                    switch (turnData.result()) {
                        case Player1Won: {
                            turnData
                                    .gameData()
                                    .getPlayer1()
                                    .getAccount()
                                    .increase(DefaultValues.GOLD_ONE_TURN);
                            turnData.increaseScore1(DefaultValues.POINTS_WINNING);
                            turnData.increaseScore2(DefaultValues.POINTS_LOSING);
                            break;
                        }
                        case Player2Won: {
                            turnData
                                    .gameData()
                                    .getPlayer2()
                                    .getAccount()
                                    .increase(DefaultValues.GOLD_ONE_TURN);
                            turnData.increaseScore2(DefaultValues.POINTS_WINNING);
                            turnData.increaseScore1(DefaultValues.POINTS_LOSING);
                            break;
                        }
                        default: {
                            turnData
                                    .gameData()
                                    .getPlayer1()
                                    .getAccount()
                                    .increase(DefaultValues.GOLD_ONE_DRAW);
                            turnData
                                    .gameData()
                                    .getPlayer2()
                                    .getAccount()
                                    .increase(DefaultValues.GOLD_ONE_DRAW);
                            turnData.increaseScore1(DefaultValues.POINTS_DRAW);
                            turnData.increaseScore2(DefaultValues.POINTS_DRAW);
                            break;
                        }
                    }

                    // result for Player1 and observers
                    String resultText1;
                    if (turnData.result().equals(EventResults.Player1Won)) resultText1 = UIEvent.GREATER.getTitle();
                    else if (turnData.result().equals(EventResults.Player2Won)) resultText1 = UIEvent.LESS.getTitle();
                    else resultText1 = UIEvent.EQUAL.getTitle();
                    val resultInfo1 = UIResultInfo.builder()
                            .figure1(UIFigure.from(turnData.figure1()))
                            .figure2(UIFigure.from(turnData.figure2()))
                            .resultText(resultText1)
                            .money1(turnData.gameData().getPlayer1().getAccount().getSum())
                            .money2(turnData.gameData().getPlayer2().getAccount().getSum())
                            .count1(turnData.score1())
                            .count2(turnData.score2())
                            .get();
                    // result for Player2
                    String resultText2;
                    if (turnData.result().equals(EventResults.Player1Won)) resultText2 = UIEvent.LESS.getTitle();
                    else if (turnData.result().equals(EventResults.Player2Won))
                        resultText2 = UIEvent.GREATER.getTitle();
                    else resultText2 = UIEvent.EQUAL.getTitle();
                    val resultInfo2 = UIResultInfo.builder()
                            .figure1(UIFigure.from(turnData.figure2()))
                            .figure2(UIFigure.from(turnData.figure1()))
                            .resultText(resultText2)
                            .money1(turnData.gameData().getPlayer2().getAccount().getSum())
                            .money2(turnData.gameData().getPlayer1().getAccount().getSum())
                            .count1(turnData.score2())
                            .count2(turnData.score1())
                            .get();
                    turnData.nextTurn();

                    val sessionIdPlayer1 = sessionKeeper.get(turnData.gameData().getPlayer1());
                    template.convertAndSend(
                            Routes.TOPIC_GAME_TURN_RESULT + "." + sessionIdPlayer1,
                            resultInfo1);

                    val sessionIdPlayer2 = sessionKeeper.get(turnData.gameData().getPlayer2());
                    template.convertAndSend(
                            Routes.TOPIC_GAME_TURN_RESULT + "." + sessionIdPlayer2,
                            resultInfo2);

                    turnData.gameData().getObservers().forEach(p -> {
                        val sessionIdObserver = sessionKeeper.get(p);
                        template.convertAndSend(
                                Routes.TOPIC_GAME_TURN_RESULT + "." + sessionIdObserver,
                                resultInfo1);
                    });

                    turnData.resetBarrier();
                }
            }
        }
    }

    @MessageMapping(Routes.API_GAME_TURN_NEXT)
    public void turnNext(@DestinationVariable long gameId, String sessionId) {
        log.warn("process turn next for game {}, sessionId {}", gameId, sessionId);
        val gameData = gameDataKeeper.get(gameId);
        if (gameData.isEmpty()) throw new GameException(String.format("No game data for gameId %d", gameId));
        val turnData = gameDataKeeper.getTurnData(gameId);
        log.warn("turn data {} before barrier", turnData);

        turnData.awaitBarrier();

        if (turnData.figure1() != null || turnData.figure2() != null) {
            synchronized (this) {
                if (turnData.figure1() != null || turnData.figure2() != null) {
                    val turnReady = UITurnReady.builder()
                            .turn(turnData.currentTurn())
                            .availCheats(turnData.availCheats())
                            .get();
                    log.warn("in synchro: turn ready {} ", turnReady);
                    gameDataKeeper
                            .byGameId(gameId)
                            .forEach(id -> template.convertAndSend(Routes.TOPIC_GAME_TURN_START + "." + id, turnReady));
                    turnData.figure1(null);
                    turnData.figure2(null);

                    turnData.resetBarrier();
                    log.warn("turn data {} before exit from synchro", turnData);
                }
            }
        }
    }

}
