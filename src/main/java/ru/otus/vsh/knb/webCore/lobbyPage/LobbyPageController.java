package ru.otus.vsh.knb.webCore.lobbyPage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.domain.msClient.data.*;
import ru.otus.vsh.knb.msCore.MsClientNames;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.webCore.GameDataKeeper;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.SessionKeeper;
import ru.otus.vsh.knb.webCore.lobbyPage.data.UIGameData;
import ru.otus.vsh.knb.webCore.lobbyPage.data.UIGameSettingsData;
import ru.otus.vsh.knb.webCore.lobbyPage.data.UIJoinGameData;

import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class LobbyPageController {

    private static final String TEMPLATE_PLAYER_NAME = "playerName";
    private static final String TEMPLATE_PLAYER_SUM = "playerSum";
    private static final String TEMPLATE_LOGOUT = "logout";
    private static final String TEMPLATE_NEW_GAME = "newGame";
    private static final String TEMPLATE_AVAIL_GAMES = "availGames";
    private static final String TEMPLATE_JOIN_GAME = "joinGame";
    private static final String TEMPLATE_PLAYER_LOGIN = "playerLogin";
    private static final String TEMPLATE_SESSION_ID = "sessionId";
    private static final String LOBBY_HTML = "lobby.html";

    private final SimpMessagingTemplate template;
    private final SessionKeeper sessionKeeper;
    private final GameDataKeeper gameDataKeeper;
    private final LobbyControllerMSClient lobbyControllerMSClient;

    @GetMapping(Routes.LOBBY)
    public String getGamePage(Model model) {
        val sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Person loggedInPerson;
        if (sessionKeeper.get(sessionId).isEmpty()) {
            loggedInPerson = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            sessionKeeper.add(sessionId, loggedInPerson);
        } else {
            loggedInPerson = sessionKeeper.get(sessionId).get();
        }

        model.addAttribute(TEMPLATE_PLAYER_NAME, loggedInPerson.getName());
        model.addAttribute(TEMPLATE_PLAYER_SUM, loggedInPerson.getAccount().getSum());
        model.addAttribute(TEMPLATE_LOGOUT, Routes.API_LOGOUT);
        model.addAttribute(TEMPLATE_PLAYER_LOGIN, loggedInPerson.getLogin());
        model.addAttribute(TEMPLATE_SESSION_ID, sessionId);
        return LOBBY_HTML;
    }

    @MessageMapping(Routes.API_LOBBY_HELLO)
    public void loadData(@DestinationVariable String sessionId) {
        val loggedInPerson = sessionKeeper.get(sessionId);
        if (loggedInPerson.isEmpty()) return;
        val message = lobbyControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                new AvailableGamesForPersonData(loggedInPerson.get()), MessageType.AVAIL_GAMES,
                replay -> {
                    val data = ((AvailableGamesForPersonReplayData) replay);
                    val games = data
                            .getGames()
                            .stream()
                            .sorted()
                            .map(gameData -> new UIGameData(
                                    String.valueOf(gameData.getGame().getId()),
                                    gameData.title(),
                                    String.valueOf(gameData.getWager()),
                                    gameData.style(loggedInPerson.get()).title()))
                            .collect(Collectors.toList());
                    template.convertAndSend(Routes.TOPIC_GAMES + "." + sessionId, games);
                }
        );
        lobbyControllerMSClient.sendMessage(message);
    }

    @MessageMapping(Routes.API_GAME_START)
    public void startGame(@DestinationVariable String sessionId, UIGameSettingsData gameSettingsData) {
        val loggedInPerson = sessionKeeper.get(sessionId).orElseThrow(() -> new GameException("Session ID without a player"));
        val newGameData = NewGameData.builder()
                .player1(loggedInPerson)
                .items(Integer.parseInt(gameSettingsData.getItems()))
                .turns(Integer.parseInt(gameSettingsData.getTurns()))
                .cheats(Integer.parseInt(gameSettingsData.getCheats()))
                .wager(Integer.parseInt(gameSettingsData.getBet()))
                .get();
        val message = lobbyControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                newGameData, MessageType.NEW_GAME,
                replay -> {
                    val gameData = ((OneGameReplyData) replay).getGameData();
                    val newGame = new UIGameData(
                            String.valueOf(gameData.getGame().getId()),
                            gameData.title(),
                            String.valueOf(gameData.getWager()),
                            gameData.style(loggedInPerson).title());
                    sessionKeeper
                            .sessions()
                            .forEach(id -> {
                                if (!sessionId.equals(id)) {
                                    template.convertAndSend(Routes.TOPIC_GAMES + "." + id, Collections.singletonList(newGame));
                                }
                            });
                    gameDataKeeper.save(sessionId, gameData);
                }
        );
        lobbyControllerMSClient.sendMessage(message);
    }

    @MessageMapping(Routes.API_GAME_JOIN)
    public void joinGame(@DestinationVariable String sessionId, UIJoinGameData uiJoinGameData) {
        val loggedInPerson = sessionKeeper.get(sessionId).orElseThrow(() -> new GameException("Session ID without a player"));
        val joinGameData = JoinGameData.builder()
                .person(loggedInPerson)
                .isPlayer(Boolean.parseBoolean(uiJoinGameData.getIsPlayer()))
                .gameId(uiJoinGameData.getGameId())
                .get();
        val message = lobbyControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                joinGameData, MessageType.JOIN_GAME,
                replay -> {
                    val gameData = ((OneGameReplyData) replay).getGameData();
                    val newGame = new UIGameData(
                            String.valueOf(gameData.getGame().getId()),
                            gameData.title(),
                            String.valueOf(gameData.getWager()),
                            gameData.style(loggedInPerson).title());
                    val statusMessage = joinGameData.isPlayer() ?
                            String.format("%s достойный соперник!", joinGameData.getPerson().getName()) :
                            String.format("%s заглянул на огонек", joinGameData.getPerson().getName());
                    val player1SessionId = sessionKeeper.get(gameData.getPlayer1());
                    if (!player1SessionId.isEmpty()) {
                        template.convertAndSend(Routes.TOPIC_GAME_STATUS + "." + player1SessionId, statusMessage);
                    }
                    val player2SessionId = sessionKeeper.get(gameData.getPlayer2());
                    if (!player2SessionId.isEmpty()) {
                        template.convertAndSend(Routes.TOPIC_GAME_STATUS + "." + player2SessionId, statusMessage);
                    }
                    gameData.getObservers()
                            .stream()
                            .map(sessionKeeper::get)
                            .forEach(id -> {
                                if (!sessionId.isEmpty()) {
                                    template.convertAndSend(Routes.TOPIC_GAME_STATUS + "." + id, statusMessage);
                                }
                            });
                    if (joinGameData.isPlayer()) {
                        sessionKeeper
                                .sessions()
                                .forEach(id -> {
                                    if (!sessionId.equals(id)) {
                                        template.convertAndSend(Routes.TOPIC_GAMES_UPD + "." + id, newGame);
                                    }
                                });
                    }
                    gameDataKeeper.save(sessionId, gameData);
                }
        );
        lobbyControllerMSClient.sendMessage(message);
    }


}
