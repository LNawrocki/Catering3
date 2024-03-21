package pl.nawrockiit.catering3.admin;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.nawrockiit.catering3.actualOrder.ActualOrder;
import pl.nawrockiit.catering3.actualOrder.ActualOrderService;
import pl.nawrockiit.catering3.config.Config;
import pl.nawrockiit.catering3.config.ConfigService;
import pl.nawrockiit.catering3.department.DepartmentService;
import pl.nawrockiit.catering3.email.EmailDetails;
import pl.nawrockiit.catering3.email.EmailService;
import pl.nawrockiit.catering3.info.InfoService;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.newOrder.NewOrderService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;
import pl.nawrockiit.catering3.xlsx.ExcelExportService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@SessionAttributes({"msg", "searchId", "searchUsername", "searchDepartmentId", "pageId"})

public class AdminController {
    private final Map<String, String> companyData;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;
    private final ConfigService configService;
    private final NewOrderService newOrderService;
    private final NewMenuService newMenuService;
    private final ActualOrderService actualOrderService;
    private final EmailService emailService;
    private final ExcelExportService excelExportService;
    private final InfoService infoService;

    public AdminController(@Value("#{${companyData}}") Map<String, String> companyData, UserService userService, DepartmentService departmentService, PasswordEncoder passwordEncoder, ConfigService configService, NewOrderService newOrderService, NewMenuService newMenuService, ActualOrderService actualOrderService, EmailService emailService, ExcelExportService excelExportService, InfoService infoService) {
        this.companyData = companyData;
        this.userService = userService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
        this.configService = configService;
        this.newOrderService = newOrderService;
        this.newMenuService = newMenuService;
        this.actualOrderService = actualOrderService;
        this.emailService = emailService;
        this.excelExportService = excelExportService;
        this.infoService = infoService;
    }

    @GetMapping("/home")
    public String adminHomeView(Model model, Principal principal) {
        model.addAttribute("companyData", companyData);

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("pageId", 2);
        model.addAttribute("infos", infoService.findAll());

        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        if (configService.editModeStatus()) {
            model.addAttribute("user", user);

            if (order != null && !order.getIsPaid()) {
                model.addAttribute("receivables", order.getToPay());
            } else {
                model.addAttribute("receivables", 0.00);
            }
            return "/admin/admin-home-editmode";
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
        return "/admin/admin-home";
    }

    @GetMapping("/addUser")
    public String addUserView(Model model) {
        User user = new User();
        user.setActive(true);
        user.setCredit(BigDecimal.valueOf(0.00));
        model.addAttribute("user", user);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("firstEmptyIndex", userService.firsEmptyIndex());
        model.addAttribute("lastEmptyIndex", userService.lastEmptyIndex());
        model.addAttribute("pageId", 6);
        model.addAttribute("companyData", companyData);

        return "/admin/user-add";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("companyData", companyData);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("firstEmptyIndex", userService.firsEmptyIndex());
            model.addAttribute("lastEmptyIndex", userService.lastEmptyIndex());


            return "/admin/user-add";
        }

        if (Objects.nonNull(userService.getUserById(user.getUserId()))) {
            model.addAttribute("msg", "Użytkownik o podanym numerze ID już istnieje");
            return "admin/admin-user-add-info-exist";
        }

        if (Objects.nonNull(userService.getUserByUsername(user.getUsername()))) {
            model.addAttribute("msg", "Użytkownik o podanym loginie już istnieje");
            return "admin/admin-user-add-info-exist";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin/addUser";
    }

    @GetMapping("/config")
    public String configView(Model model) {
        Config config = configService.getConfig();
        model.addAttribute(config);
        model.addAttribute("pageId", 13);
        model.addAttribute("companyData", companyData);
        return "/admin/admin-config";
    }

    @PostMapping("/config/editMode")
    public String configEditMenu(@RequestParam Boolean editMode) {
        Config configValues = configService.getConfig();
        configValues.setEditMode(editMode);
        configService.save(configValues);
        return "redirect:/admin/config";
    }


    @GetMapping("/userList")
    public String userList(Model model, HttpSession session, SessionStatus status) {
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("pageId", 5);
        model.addAttribute("companyData", companyData);
        List<User> allUsers = userService.findAll();
        BigDecimal accountsCreditSum = BigDecimal.valueOf(0);

        for (User user : allUsers) {
            accountsCreditSum = accountsCreditSum.add(user.getCredit());
        }
        model.addAttribute("accountsCreditSum", accountsCreditSum);


        if (session.getAttribute("searchId") != null && session.getAttribute("searchId") != "") {
            Integer searchUserId = Integer.parseInt((String) session.getAttribute("searchId"));
            User user = userService.getUserById(searchUserId);
            status.setComplete();
            List<User> users = new ArrayList<>();
            users.add(user);
            model.addAttribute("usersList", users);
            model.addAttribute("searchId", null);
            return "/admin/user-list";
        }

        if (session.getAttribute("searchUsername") != null && session.getAttribute("searchUsername") != "") {
//            List<User> allUsers = userService.findAll();
            List<User> foundUsers = new ArrayList<>();
            for (User user : allUsers) {
                if (user.getUsername().contains((String) session.getAttribute("searchUsername"))) {
                    foundUsers.add(user);
                }
            }
            status.setComplete();
            model.addAttribute("usersList", foundUsers);
            model.addAttribute("searchUsername", null);
            return "/admin/user-list";
        }

        if (session.getAttribute("searchDepartmentId") != null && session.getAttribute("searchDepartmentId") != "") {
            List<User> users = userService.findUsersByDepartment(departmentService.getById((Integer) session.getAttribute("searchDepartmentId")));
            status.setComplete();
            model.addAttribute("usersList", users);
            return "/admin/user-list";
        }


        model.addAttribute("usersList", allUsers);
        return "/admin/user-list";
    }


    @PostMapping("/userList/searchId")
    public String userListId(@RequestParam String searchId, Model model) {
        model.addAttribute("searchId", searchId);
        model.addAttribute("companyData", companyData);
        return "redirect:/admin/userList";
    }

    @PostMapping("/userList/searchUsername")
    public String userListLogin(@RequestParam String searchUsername, Model model) {
        model.addAttribute("searchUsername", searchUsername);
        model.addAttribute("companyData", companyData);
        return "redirect:/admin/userList";
    }

    @PostMapping("/userList/searchDepartment")
    public String userListDepartment(@RequestParam Integer searchDepartmentId, Model model) {
        model.addAttribute("searchDepartmentId", searchDepartmentId);
        model.addAttribute("companyData", companyData);
        return "redirect:/admin/userList";
    }

    @PostMapping("/userList/searchClean")
    public String userListClean(Model model) {
        model.addAttribute("searchId", "");
        model.addAttribute("searchUsername", "");
        model.addAttribute("searchDepartmentId", "");
        model.addAttribute("companyData", companyData);
        return "redirect:/admin/userList";
    }

    @PostMapping("/deleteUser/confirm")
    public String deleteConfirm(@RequestParam Integer deleteUserId, @RequestParam(required = false) Boolean confirm, Principal principal, Model model, HttpSession session) {
        model.addAttribute("companyData", companyData);
        User user = userService.getUserById(deleteUserId);

        if (user.getRole().equals("ROLE_ADMIN")) {
            return "/admin/admin-delete-info";
        }

        ActualOrder actualOrderByUserId = actualOrderService.getActualOrderByUserId(deleteUserId);
        NewOrder newOrder = newOrderService.getNewOrderByUserId(deleteUserId);
        if ((actualOrderByUserId != null || newOrder != null) && Objects.isNull(confirm)) {
            model.addAttribute("deleteUserId", deleteUserId);
            return "/admin/user-list-delete-info";
        }

        if (actualOrderByUserId != null) {
            actualOrderService.delete(actualOrderByUserId);
        }
        if (newOrder != null) {
            newOrderService.delete(newOrder);
        }
        userService.delete(user);
        model.addAttribute("usersList", userService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "redirect:/admin/userList";
    }


    @GetMapping("/userUpdate")
    public String adminUpdateView(@RequestParam Integer editUserId, Model model) {
        User user = userService.getUserById(editUserId);
        model.addAttribute("user", user);
        model.addAttribute("editUserId", user.getUserId());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("companyData", companyData);
        return "/admin/admin-update";
    }

    @PostMapping("/userUpdate")
    public String adminUpdate(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("companyData", companyData);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            model.addAttribute("editUserId", user.getUserId());
            model.addAttribute("departments", departmentService.findAll());
            return "/admin/admin-update";
        }

        if (user.getPassword().equals(userService.getUserById(user.getUserId()).getPassword())) {
            userService.updateUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getDepartment(), user.getPassword(), user.getRole(),
                    user.getActive(), user.getEmail(), user.getCredit(), user.getUserId());

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - aktualizacja danych dla " + user.getUsername());
            emailDetails.setMsgBody("Zaktualizowałeś dane użytkownika dla konta " + user.getUsername());
            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
            return "redirect:/admin/userList";

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - aktualizacja danych dla " + user.getUsername());
            emailDetails.setMsgBody("Zaktualizowałeś dane użytkownika dla konta " + user.getUsername());
            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);

            return "redirect:/admin/userList";
        }
    }

    @PostMapping("/getUsersListInXls")
    public void getUsersListInXls(HttpServletResponse servletResponse) throws IOException {
        excelExportService.getUsersListInXls(servletResponse);
    }

    @GetMapping("/email/notification")
    public String emailNotificationView(Model model) {

        model.addAttribute("companyData", companyData);

        return "/admin/email-notification";
    }

    @PostMapping("/email/notification")
    public String emailNotificationList(@RequestParam String infoTitle,
                                        @RequestParam String infoMessage) {
        if (!infoTitle.equals("") && infoTitle != null && !infoMessage.equals("") && infoMessage != null) {
            EmailDetails emailDetails = new EmailDetails();
            List<String> recipients = new ArrayList<>();
            recipients.add(companyData.get("companyEmail"));
            emailDetails.setSubject(infoTitle);
            emailDetails.setMsgBody(infoMessage);
            List<User> allUsers = userService.findAll();
            for (User user : allUsers) {
                if (user.getEmail() != null && !user.getEmail().equals("")) {
                    recipients.add(user.getEmail());
                }
            }
            emailService.sendMailRecipientList(emailDetails, recipients);
        } else {
            return "redirect:/admin/email/notification";
        }
        return "redirect:/admin/config";
    }

    @GetMapping("/userList/sendMessage")
    public String emailUserNotificationView(@RequestParam Integer userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("companyData", companyData);
        return "/admin/email-to-user";
    }

    @PostMapping("/userList/sendMesage")
    public String emailUserNotificationList(@RequestParam(required = false) String infoTitle,
                                            @RequestParam(required = false) String infoMessage,
                                            @RequestParam String userEmail,
                                            @RequestParam Integer userId, Model model) {
        if (!infoTitle.equals("") && infoTitle != null && !infoMessage.equals("") && infoMessage != null) {
            EmailDetails emailDetails = new EmailDetails();
            List<String> recipients = new ArrayList<>();
            recipients.add(companyData.get("companyEmail"));
            recipients.add(userEmail);
            emailDetails.setSubject(infoTitle);
            emailDetails.setMsgBody(infoMessage);
            emailService.sendMailRecipientList(emailDetails, recipients);
        } else {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            model.addAttribute("companyData", companyData);
            return "redirect:/admin/userList/sendMessage?userId=" + user.getUserId();
        }
        return "redirect:/admin/userList";
    }
}