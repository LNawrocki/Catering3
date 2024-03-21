package pl.nawrockiit.catering3.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.nawrockiit.catering3.config.ConfigService;
import pl.nawrockiit.catering3.department.DepartmentService;
import pl.nawrockiit.catering3.email.EmailDetails;
import pl.nawrockiit.catering3.email.EmailService;
import pl.nawrockiit.catering3.info.InfoService;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.newOrder.NewOrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
@SessionAttributes({"userId", "firstName", "lastName", "searchId", "searchLogin", "searchDepartmentId", "pageId"})
public class UserController {


    public final DepartmentService departmentService;
    public final NewOrderService newOrderService;
    private final UserService userService;
    private final NewMenuService newMenuService;
    private final ConfigService configService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Map<String, String> companyData;
    private final InfoService infoService;

    public UserController(DepartmentService departmentService, NewOrderService newOrderService, UserService userService, NewMenuService newMenuService, ConfigService configService, PasswordEncoder passwordEncoder, EmailService emailService, @Value("#{${companyData}}") Map<String,  String > companyData, InfoService infoService) {
        this.departmentService = departmentService;
        this.newOrderService = newOrderService;
        this.userService = userService;
        this.newMenuService = newMenuService;
        this.configService = configService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.companyData = companyData;
        this.infoService = infoService;
    }

    @GetMapping("/home")
    public String userHomeView(Model model, Principal principal, HttpSession session) {

        model.addAttribute("companyData", companyData);

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("pageId", 2);
        model.addAttribute("infos", infoService.findAll());

        if (configService.getConfig().getEditMode()) {
            model.addAttribute("user", user);
            if (order != null && !order.getIsPaid()) {
                model.addAttribute("receivables", order.getToPay());
            } else {
                model.addAttribute("receivables", 0.00);
            }
            return "/user/user-home-editmode";
        }

        if (order != null && !order.getIsPaid()) {
            model.addAttribute("receivables", order.getToPay());
        } else {
            model.addAttribute("receivables", 0.00);
        }

        model.addAttribute("mealsMonday", newMenuService.newMenuFindByDayId(1));
        model.addAttribute("mealsTuesday", newMenuService.newMenuFindByDayId(2));
        model.addAttribute("mealsWednesday", newMenuService.newMenuFindByDayId(3));
        model.addAttribute("mealsThursday", newMenuService.newMenuFindByDayId(4));
        model.addAttribute("mealsFriday", newMenuService.newMenuFindByDayId(5));
        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }

        return "/user/user-home";
    }

    @GetMapping("/update")
    public String userUpdateView(@RequestParam Integer editUserId, Model model) {
        model.addAttribute("companyData", companyData);
        User user = userService.getUserById(editUserId);
//        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("editUserId", user.getUserId());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("pageId", 5);
        return "/user/user-update";
    }

    @PostMapping("/update")
    public String userUpdate(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("companyData", companyData);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            model.addAttribute("editUserId", user.getUserId());
            model.addAttribute("departments", departmentService.findAll());
            return "/user/user-update";
        }
        if (user.getPassword().equals(userService.getUserByUsername(user.getUsername()).getPassword())){
            userService.updateUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getDepartment() , user.getPassword(), user.getRole(),
                    user.getActive(), user.getEmail(), user.getCredit(), user.getUserId());
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - aktualizacja danych dla " + user.getUsername());
            emailDetails.setMsgBody("Zaktualizowałeś dane użytkownika dla konta " + user.getUsername());
            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
            return "redirect:/user/home";

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - aktualizacja danych dla " + user.getUsername());
            emailDetails.setMsgBody("Zaktualizowałeś dane użytkownika dla konta " + user.getUsername());
            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
            return "redirect:/logout";
        }
    }
}