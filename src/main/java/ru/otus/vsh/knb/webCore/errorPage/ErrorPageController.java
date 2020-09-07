package ru.otus.vsh.knb.webCore.errorPage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.vsh.knb.webCore.Routes;

@Controller
@Slf4j
public class ErrorPageController {

    private static final String ERROR_PAGE_TEMPLATE = "error.html";

    @GetMapping(Routes.ERROR)
    public String getLoginPage(Model model) {
        return ERROR_PAGE_TEMPLATE;
    }

}
