package ru.otus.vsh.knb.webCore.gamePage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.SessionKeeper;

@Controller
@AllArgsConstructor
@Slf4j
public class GamePageController {
    private static final String TEMPLATE_PLAYER_NAME = "playerName";
    private static final String GAME_HTML = "game.html";


    private final SimpMessagingTemplate template;
    private final SessionKeeper sessionKeeper;

    @GetMapping(Routes.GAME)
    public String getGamePage(Model model) {
        val sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        val loggedInPerson = sessionKeeper.get(sessionId).orElseThrow(() -> new GameException("Session id without a player"));

        model.addAttribute(TEMPLATE_PLAYER_NAME, loggedInPerson.getName());
        return GAME_HTML;
    }
}
