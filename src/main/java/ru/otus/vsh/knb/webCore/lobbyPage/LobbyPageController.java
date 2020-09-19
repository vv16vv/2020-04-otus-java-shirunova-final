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
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.domain.msClient.data.AvailableGamesForPersonData;
import ru.otus.vsh.knb.domain.msClient.data.AvailableGamesForPersonReplayData;
import ru.otus.vsh.knb.domain.msClient.data.NewGameData;
import ru.otus.vsh.knb.domain.msClient.data.NewGameReplyData;
import ru.otus.vsh.knb.msCore.MsClientNames;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.SessionKeeper;
import ru.otus.vsh.knb.webCore.lobbyPage.data.UIGameData;

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
    private final LobbyControllerMSClient lobbyControllerMSClient;

    @GetMapping(Routes.LOBBY)
    public String getGamePage(Model model) {
        Person loggedInPerson = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        sessionKeeper.add(sessionId, loggedInPerson);

        model.addAttribute(TEMPLATE_PLAYER_NAME, loggedInPerson.getName());
        model.addAttribute(TEMPLATE_PLAYER_SUM, loggedInPerson.getAccount().getSum());
        model.addAttribute(TEMPLATE_LOGOUT, Routes.API_LOGOUT);
        model.addAttribute(TEMPLATE_PLAYER_LOGIN, loggedInPerson.getLogin());
        model.addAttribute(TEMPLATE_SESSION_ID, sessionId);
        return LOBBY_HTML;
    }

    @MessageMapping(Routes.API_LOBBY_HELLO)
    public void loadData(@DestinationVariable String sessionId) {
        val loggedInPerson = sessionKeeper.get(sessionId).orElseThrow(() -> new GameException("Session ID without a player"));
        val message = lobbyControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                new AvailableGamesForPersonData(loggedInPerson), MessageType.AVAIL_GAMES,
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
                                    gameData.style(loggedInPerson).title()))
                            .collect(Collectors.toList());
                    template.convertAndSend(Routes.TOPIC_GAMES + "." + sessionId, games);
                }
        );
        lobbyControllerMSClient.sendMessage(message);
    }

    @MessageMapping(Routes.API_GAME_START)
    public RedirectView startGame(@DestinationVariable String sessionId) {
        val loggedInPerson = sessionKeeper.get(sessionId).orElseThrow(() -> new GameException("Session ID without a player"));
        val message = lobbyControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                NewGameData.builder().player1(loggedInPerson).get(), MessageType.NEW_GAME,
                replay -> {
                    val gameData = ((OneGameReplyData) replay).getGameData();
                    val newGame = new UIGameData(
                            String.valueOf(gameData.getGame().getId()),
                            gameData.title(),
                            String.valueOf(gameData.getWager()),
                            gameData.style(loggedInPerson).title());
                    template.convertAndSend(Routes.TOPIC_GAME_STATUS + "." + sessionId, "Кто еще захочет играть с тобой? Ждем...");
                    sessionKeeper
                            .sessions()
                            .forEach(id -> {
                                if (!sessionId.equals(id)) {
                                    template.convertAndSend(Routes.TOPIC_GAMES + "." + id, Collections.singletonList(newGame));
                                }
                            });

                }
        );
        lobbyControllerMSClient.sendMessage(message);

        return new RedirectView(Routes.GAME, true);
    }


}
