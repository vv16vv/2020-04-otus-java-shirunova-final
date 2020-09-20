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
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.webCore.GameDataKeeper;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.SessionKeeper;
import ru.otus.vsh.knb.webCore.gamePage.data.UIGameInitialInfo;

@Controller
@AllArgsConstructor
@Slf4j
public class GamePageController {
    private static final String TEMPLATE_PLAYER_NAME = "playerName";
    private static final String GAME_HTML = "game.html";
    private static final String TEMPLATE_SESSION_ID = "sessionId";

    private final SimpMessagingTemplate template;
    private final SessionKeeper sessionKeeper;

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
        val loggedInPerson = sessionKeeper.get(sessionId);
        if (loggedInPerson.isEmpty()) return;
        val gameData = gameDataKeeper.get(sessionId);
        if (gameData.isEmpty()) return;
        val initialGameInfo = UIGameInitialInfo.builder()
                .playerName1(gameData.get().getPlayer1().getName())
                .money1(gameData.get().getPlayer1().getAccount().getSum())
                .turns(gameData.get().getGame().getSettings().getNumberOfTurns())
                .cheats(gameData.get().getGame().getSettings().getNumberOfCheats())
                .availCheats(gameData.get().getGame().getSettings().getNumberOfCheats())
                .bet(gameData.get().getWager());
        if (gameData.get().getPlayer2() != null) {
            initialGameInfo
                    .playerName2(gameData.get().getPlayer2().getName())
                    .money2(gameData.get().getPlayer2().getAccount().getSum());
        }

        gameDataKeeper
                .byGame(gameData.get().getGame())
                .forEach(id -> template.convertAndSend(Routes.TOPIC_GAME_INFO + "." + id, initialGameInfo.get()));
    }

}
