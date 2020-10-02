package ru.otus.vsh.knb.webCore.newPlayerPage;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerData;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerReplyData;
import ru.otus.vsh.knb.msCore.MessageSystemHelper;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.msCore.MsClientNames;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@AllArgsConstructor
public class NewPlayerPageController {
    private static final String TEMPLATE_ROUTE = "route";
    private static final String TEMPLATE_PLAYER = "player";
    private static final String PLAYER_PAGE_TEMPLATE = "new-player.html";

    private final NewPlayerControllerMSClient newPlayerControllerMSClient;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(Routes.NEW_PLAYER)
    public String getNewUserPage(Model model) {
        model.addAttribute(TEMPLATE_ROUTE, Routes.NEW_PLAYER);
        model.addAttribute(TEMPLATE_PLAYER, new NewPlayerData());
        return PLAYER_PAGE_TEMPLATE;
    }

    @PostMapping(Routes.NEW_PLAYER)
    public RedirectView addUser(@ModelAttribute NewPlayerData playerData) {
        playerData.setPassword(passwordEncoder.encode(playerData.getPassword()));
        val latch = new CountDownLatch(1);
        AtomicReference<String> redirectView = new AtomicReference<>(Routes.ROOT);
        val message = newPlayerControllerMSClient.produceMessage(
                MsClientNames.DATA_BASE.name(),
                playerData, MessageType.NEW_PLAYER,
                replay -> {
                    if (!((NewPlayerReplyData) replay).isUserAdded()) {
                        redirectView.set(Routes.NEW_PLAYER);
                    }
                    latch.countDown();
                }
        );
        newPlayerControllerMSClient.sendMessage(message);

        MessageSystemHelper.waitForAnswer(latch);

        return new RedirectView(redirectView.get(), true);
    }

}
