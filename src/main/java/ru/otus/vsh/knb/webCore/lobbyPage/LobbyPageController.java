package ru.otus.vsh.knb.webCore.lobbyPage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.Id;
import ru.otus.vsh.knb.webCore.Routes;

@Controller
@AllArgsConstructor
@Slf4j
public class LobbyPageController {

    private static final String TEMPLATE_PLAYER_NAME = "playerName";
    private static final String TEMPLATE_LOGOUT = "logout";
    private static final String TEMPLATE_SESSION_ID = "sessionId";
    private static final String LOBBY_HTML = "lobby.html";

    @GetMapping(Routes.LOBBY)
    public String getGamePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute(TEMPLATE_PLAYER_NAME, ((Person) authentication.getPrincipal()).getName());
        model.addAttribute(TEMPLATE_LOGOUT, Routes.API_LOGOUT);
        model.addAttribute(TEMPLATE_SESSION_ID, new Id().getInnerId());

        return LOBBY_HTML;
    }
}
