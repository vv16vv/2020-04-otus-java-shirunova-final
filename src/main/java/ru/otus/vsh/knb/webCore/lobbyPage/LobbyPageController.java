package ru.otus.vsh.knb.webCore.lobbyPage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.domain.msClient.data.AvailableGamesForPersonData;
import ru.otus.vsh.knb.domain.msClient.data.AvailableGamesForPersonReplayData;
import ru.otus.vsh.knb.msCore.MessageSystemHelper;
import ru.otus.vsh.knb.msCore.MsClientNames;
import ru.otus.vsh.knb.msCore.common.Id;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.lobbyPage.data.UIGameData;

import java.util.concurrent.CountDownLatch;
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
    private static final String TEMPLATE_SESSION_ID = "sessionId";
    private static final String LOBBY_HTML = "lobby.html";

    private final LobbyControllerMSClient lobbyControllerMSClient;

    @GetMapping(Routes.LOBBY)
    public String getGamePage(Model model) {
        Person loggedInPerson = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        val latch = new CountDownLatch(1);
        val message = lobbyControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                new AvailableGamesForPersonData(loggedInPerson), MessageType.AVAIL_GAMES,
                replay -> {
                    val data = ((AvailableGamesForPersonReplayData) replay)
                            .getGames()
                            .stream()
                            .sorted()
                            .map(gameData -> new UIGameData(gameData.title(), gameData.style(loggedInPerson).title()))
                            .collect(Collectors.toList());
                    model.addAttribute(TEMPLATE_PLAYER_NAME, loggedInPerson.getName());
                    model.addAttribute(TEMPLATE_PLAYER_SUM, loggedInPerson.getAccount().getSum());
                    model.addAttribute(TEMPLATE_LOGOUT, Routes.API_LOGOUT);
                    model.addAttribute(TEMPLATE_NEW_GAME, Routes.ERROR);
                    model.addAttribute(TEMPLATE_AVAIL_GAMES, data);
                    model.addAttribute(TEMPLATE_JOIN_GAME, Routes.ERROR);
                    model.addAttribute(TEMPLATE_SESSION_ID, new Id().getInnerId());
                    latch.countDown();
                }
        );
        lobbyControllerMSClient.sendMessage(message);

        MessageSystemHelper.waitForAnswer(latch);

        return LOBBY_HTML;
    }
}
