package ru.otus.vsh.knb.webCore.loginPage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.vsh.knb.webCore.Routes;

@Controller
@AllArgsConstructor
public class LoginPageController {
    private static final String TEMPLATE_LOGIN_FORM = "login";
    private static final String TEMPLATE_LOGIN_SIGNON = "signon";
    private static final String TEMPLATE_LOGIN_PLAYERS = "players";
    private static final String INDEX_PAGE_TEMPLATE = "index.html";

    @GetMapping(Routes.ROOT)
    public String getLoginPage(Model model) {
        model.addAttribute(TEMPLATE_LOGIN_FORM, Routes.API_LOGIN);
        model.addAttribute(TEMPLATE_LOGIN_SIGNON, Routes.NEW_PLAYER);
        model.addAttribute(TEMPLATE_LOGIN_PLAYERS, Routes.PLAYERS);
        return INDEX_PAGE_TEMPLATE;
    }

}
