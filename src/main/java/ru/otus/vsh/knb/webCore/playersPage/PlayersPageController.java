package ru.otus.vsh.knb.webCore.playersPage;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.vsh.knb.domain.msClient.data.PlayersReplyData;
import ru.otus.vsh.knb.msCore.MessageSystemHelper;
import ru.otus.vsh.knb.msCore.common.EmptyMessageData;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.msCore.MsClientNames;

import java.util.concurrent.CountDownLatch;

@Controller
@AllArgsConstructor
public class PlayersPageController {

    private static final String TEMPLATE_PLAYERS = "players";
    private static final String TEMPLATE_LOGIN = "login";
    private static final String PLAYERS_PAGE_TEMPLATE = "players.html";

    private final PlayersControllerMSClient playersControllerMSClient;

    @GetMapping(Routes.PLAYERS)
    public String getPlayersPage(Model model) {
        val latch = new CountDownLatch(1);
        val message = playersControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                new EmptyMessageData(), MessageType.PLAYERS,
                replay -> {
                    model.addAttribute(TEMPLATE_LOGIN, Routes.ROOT);
                    model.addAttribute(TEMPLATE_PLAYERS, ((PlayersReplyData) replay).getPlayers());
                    latch.countDown();
                }
        );
        playersControllerMSClient.sendMessage(message);

        MessageSystemHelper.waitForAnswer(latch);

        return PLAYERS_PAGE_TEMPLATE;
    }

}
