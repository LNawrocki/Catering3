package pl.nawrockiit.catering3.actualOrder;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import pl.nawrockiit.catering3.department.DepartmentService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"searchUsername", "searchDepartmentId"})

public class ActualOrderController {
    private final Map<String, String> companyData;
    private final ActualOrderRepository actualOrderRepository;
    private final UserService userService;
    private final ActualOrderService actualOrderService;
    private final DepartmentService departmentService;

    public ActualOrderController(@Value("#{${companyData}}") Map<String, String> companyData, ActualOrderRepository actualOrderRepository, UserService userService, ActualOrderService actualOrderService, DepartmentService departmentService) {
        this.companyData = companyData;
        this.actualOrderRepository = actualOrderRepository;
        this.userService = userService;
        this.actualOrderService = actualOrderService;
        this.departmentService = departmentService;
    }

    @GetMapping("/user/actualOrder")
    public String userActualOrderView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("companyData", companyData);
        model.addAttribute("user", user);
        model.addAttribute("actualOrder",
                actualOrderRepository.getActualOrderByUser_UserId(user.getUserId()));
        model.addAttribute("pageId", 4);

        return "/user/actual-order-user";
    }

    @GetMapping("/admin/actualOrder")
    public String adminActualOrderView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("companyData", companyData);
        model.addAttribute("user", user);
        model.addAttribute("actualOrder",
                actualOrderRepository.getActualOrderByUser_UserId(user.getUserId()));
        model.addAttribute("pageId", 4);

        return "/admin/actual-order-admin";

    }

    @GetMapping("/admin/actualOrder/list")
    public String actualOrderListView(Model model, HttpSession session, SessionStatus status) {
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("pageId", 10);
        model.addAttribute("companyData", companyData);


        if (session.getAttribute("searchDepartmentId") != null && session.getAttribute("searchDepartmentId") != "" && (Integer) session.getAttribute("searchDepartmentId") != 0) {
            List<User> users = userService.findUsersByDepartment(departmentService.getDepartmentById((Integer) session.getAttribute("searchDepartmentId")));
            List<ActualOrder> actualOrders = new ArrayList<>();
            for (User user : users) {
                if (actualOrderService.getActualOrderByUserId(user.getUserId()) != null) {
                    actualOrders.add(actualOrderService.getActualOrderByUserId(user.getUserId()));
                }
            }

            status.setComplete();
            model.addAttribute("actualOrders", actualOrders);
            model.addAttribute("period", actualOrderService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", actualOrderService.findAll().get(0).getPeriodDate());
            return "/admin/admin-actual-order-list";
        }

        if (session.getAttribute("searchUsername") != null && session.getAttribute("searchUsername") != "") {
            List<ActualOrder> all = actualOrderService.findAll();
            List<ActualOrder> actualOrders = new ArrayList<>();
            for (ActualOrder actualOrder : all) {
                if (actualOrder.getUser().getUsername().contains((String) session.getAttribute("searchUsername"))) {
                    actualOrders.add(actualOrder);
                }
            }
            status.setComplete();
            model.addAttribute("actualOrders", actualOrders);
            if (!actualOrderService.findAll().isEmpty()) {
                model.addAttribute("period", actualOrderService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", actualOrderService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }
            return "/admin/admin-actual-order-list";
        }
        if (!actualOrderService.findAll().isEmpty()) {
            model.addAttribute("period", actualOrderService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", actualOrderService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }
        model.addAttribute("actualOrders", actualOrderService.findAll());
        return "/admin/admin-actual-order-list";
    }

    @PostMapping("/admin/actualOrderList/searchUsername")
    public String userListLogin(@RequestParam String searchUsername, Model model) {
        model.addAttribute("searchUsername", searchUsername);
        model.addAttribute("companyData", companyData);
        return "redirect:/admin/actualOrder/list";
    }

    @PostMapping("/admin/actualOrderList/searchDepartment")
    public String userListDepartment(@RequestParam Integer searchDepartmentId, Model model) {
        model.addAttribute("searchDepartmentId", searchDepartmentId);
        model.addAttribute("companyData", companyData);
        return "redirect:/admin/actualOrder/list";
    }
}