package pl.nawrockiit.catering3.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Controller
public class LoginController {

    private final UserService userService;
    private final Map<String, String> companyData;

    public LoginController(@Value("#{${companyData}}") Map<String, String> companyData, UserService userService) {
        this.userService = userService;
        this.companyData = companyData;
    }

    @GetMapping("/login")
    public String getLoginView(Model model, @RequestParam (required = false) String msg, HttpSession session) {
        model.addAttribute("companyData", companyData);
        model.addAttribute("msg", msg);
        model.addAttribute("pageId", 1);

        return "/login";
    }

//    Potrzebne tylko przy włączonej obsłudze CSRF w SecurityConfig
//    Wtedy login może być realizowany tylko z wykorzystaniem żądań typu POST, a nie GET
//    @GetMapping("/logout")
//    public String getLogoutView() {
//        return "/logout-view";
//    }
}
