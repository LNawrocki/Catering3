package pl.nawrockiit.catering3.home;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.nawrockiit.catering3.config.Config;
import pl.nawrockiit.catering3.config.ConfigService;
import pl.nawrockiit.catering3.department.Department;
import pl.nawrockiit.catering3.department.DepartmentService;
import pl.nawrockiit.catering3.info.InfoService;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class HomeController {

    private final Map<String, String> companyData;
    private final ConfigService configService;
    private final NewMenuService newMenuService;
    private final InfoService infoService;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;

    public HomeController(@Value("#{${companyData}}") Map<String, String> companyData, ConfigService configService, NewMenuService newMenuService, InfoService infoService, UserService userService, DepartmentService departmentService, PasswordEncoder passwordEncoder) {
        this.companyData = companyData;
        this.configService = configService;
        this.newMenuService = newMenuService;
        this.infoService = infoService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String homeView(Model model) {
        model.addAttribute("companyData", companyData);
        if (configService.findAll().isEmpty() || userService.findAll().isEmpty()) {
            Department department = new Department();
            department.setId(0);
            department.setName("Domyślny");
            department.setPaymentPerc(100);
            departmentService.save(department);

            Config config = new Config();
            config.setEditMode(true);
            configService.save(config);

            User user = new User();
            user.setDepartment(department);
            user.setActive(true);
            user.setRole("ROLE_ADMIN");
            model.addAttribute("user", user);
            model.addAttribute("departments", departmentService.findAll());
            return "admin/initPage";
        }


        if (configService.editModeStatus()) {
            model.addAttribute("infos", infoService.findAll());
            model.addAttribute("pageId", 0);
            model.addAttribute("infos", infoService.findAll());
            return "home-editmode";
        }
        model.addAttribute("mealsMonday", newMenuService.newMenuFindByDayId(1));
        model.addAttribute("mealsTuesday", newMenuService.newMenuFindByDayId(2));
        model.addAttribute("mealsWednesday", newMenuService.newMenuFindByDayId(3));
        model.addAttribute("mealsThursday", newMenuService.newMenuFindByDayId(4));
        model.addAttribute("mealsFriday", newMenuService.newMenuFindByDayId(5));
        model.addAttribute("pageId", 0);
        model.addAttribute("infos", infoService.findAll());

        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }
        return "home";
    }

    @PostMapping
    public String initAdminUser(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("companyData", companyData);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            model.addAttribute("departments", departmentService.findAll());
            return "/admin/initPage";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping ("/zlicz")
    public String zlicz(){

        return "zlicz";
    }


    @PostMapping ("/zlicz")
    public String zliczWynik(@RequestParam String zdanie, Model model) {
        System.out.println(zdanie);
        Map<Character, Integer> zliczeniaLiter = new HashMap<>();
        for (char znak : zdanie.toCharArray()) {
            if (znak != ' ') {
                zliczeniaLiter.put(znak, zliczeniaLiter.getOrDefault(znak, 0) + 1);
            }
        }
        for (Map.Entry<Character, Integer> entry : zliczeniaLiter.entrySet()) {
            System.out.println("Litera '" + entry.getKey() + "' występuje " + entry.getValue() + " razy.");
        }
        model.addAttribute("hashMapa", zliczeniaLiter);
        model.addAttribute("zdanie", zdanie);
        return "/zliczWynik";
    }
}
