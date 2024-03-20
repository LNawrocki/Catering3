package pl.nawrockiit.catering3.newOrder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import pl.nawrockiit.catering3.config.Config;
import pl.nawrockiit.catering3.config.ConfigService;
import pl.nawrockiit.catering3.department.DepartmentService;
import pl.nawrockiit.catering3.email.EmailDetails;
import pl.nawrockiit.catering3.email.EmailService;
import pl.nawrockiit.catering3.newMenu.NewMenu;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
@SessionAttributes({"searchNewOrderId", "searchIsPaid", "searchUsername", "searchDepartmentId", "searchUserId", "orderUserId", "userId", "msg"})
public class NewOrderController {
    private final Map<String, String> companyData;
    public final NewOrderService newOrderService;
    public final UserService userService;
    public final NewMenuService newMenuService;
    private final ConfigService configService;
    private final DepartmentService departmentService;
    private final EmailService emailService;

    public NewOrderController(@Value("#{${companyData}}") Map<String, String> companyData, NewOrderService newOrderService, UserService userService, NewMenuService newMenuService, ConfigService configService, DepartmentService departmentService, EmailService emailService) {
        this.companyData = companyData;
        this.newOrderService = newOrderService;
        this.userService = userService;
        this.newMenuService = newMenuService;
        this.configService = configService;
        this.departmentService = departmentService;
        this.emailService = emailService;
    }

    private static NewOrder getNewOrder(String period, String periodDate, User user) {
        NewOrder newOrder = new NewOrder();
        newOrder.setQtyMon(1);
        newOrder.setQtyTue(1);
        newOrder.setQtyWed(1);
        newOrder.setQtyThu(1);
        newOrder.setQtyFri(1);
        newOrder.setPeriod(period);
        newOrder.setPeriodDate(periodDate);
        newOrder.setUser(user);
        newOrder.setToPay(BigDecimal.valueOf(0));
        newOrder.setIsPaid(false);
        return newOrder;
    }

    @GetMapping("/admin/newOrder/list")
    public String orderListView(Model model, HttpSession session, SessionStatus status) {
        model.addAttribute("companyData", companyData);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("pageId", 9);

        List<NewOrder> newOrdersSum = newOrderService.findAll();

        BigDecimal firstShiftPriceSum = BigDecimal.valueOf(0);
        BigDecimal secondShiftPriceSum = BigDecimal.valueOf(0);
        BigDecimal firstShiftPaidSum = BigDecimal.valueOf(0);
        BigDecimal secondShiftPaidSum = BigDecimal.valueOf(0);

        for (NewOrder newOrder : newOrdersSum) {
            if (newOrder.getShiftMon() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(newOrder.getPriceMon());
                if (newOrder.getIsPaid()){
                    firstShiftPaidSum = firstShiftPaidSum.add(newOrder.getPriceMon());
                }
            } else if (newOrder.getShiftMon() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(newOrder.getPriceMon());
                if (newOrder.getIsPaid()){
                    secondShiftPaidSum = secondShiftPaidSum.add(newOrder.getPriceMon());
                }
            }
            if (newOrder.getShiftTue() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(newOrder.getPriceTue());
                if (newOrder.getIsPaid()){
                    firstShiftPaidSum = firstShiftPaidSum.add(newOrder.getPriceTue());
                }
            } else if (newOrder.getShiftTue() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(newOrder.getPriceTue());
                if (newOrder.getIsPaid()){
                    secondShiftPaidSum = secondShiftPaidSum.add(newOrder.getPriceTue());
                }
            }


            if (newOrder.getShiftWed() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(newOrder.getPriceWed());
                if (newOrder.getIsPaid()){
                    firstShiftPaidSum = firstShiftPaidSum.add(newOrder.getPriceWed());
                }
            } else if (newOrder.getShiftWed() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(newOrder.getPriceWed());
                if (newOrder.getIsPaid()){
                    secondShiftPaidSum = secondShiftPaidSum.add(newOrder.getPriceWed());
                }
            }


            if (newOrder.getShiftThu() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(newOrder.getPriceThu());
                if (newOrder.getIsPaid()){
                    firstShiftPaidSum = firstShiftPaidSum.add(newOrder.getPriceThu());
                }
            } else if (newOrder.getShiftThu() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(newOrder.getPriceThu());
                if (newOrder.getIsPaid()){
                    secondShiftPaidSum = secondShiftPaidSum.add(newOrder.getPriceThu());
                }
            }


            if (newOrder.getShiftFri() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(newOrder.getPriceFri());
                if (newOrder.getIsPaid()){
                    firstShiftPaidSum = firstShiftPaidSum.add(newOrder.getPriceFri());
                }
            } else if (newOrder.getShiftFri() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(newOrder.getPriceFri());
                if (newOrder.getIsPaid()){
                    secondShiftPaidSum = secondShiftPaidSum.add(newOrder.getPriceFri());
                }
            }
        }

        model.addAttribute("firstShiftPriceSum", firstShiftPriceSum);
        model.addAttribute("secondShiftPriceSum", secondShiftPriceSum);
        model.addAttribute("priceSum", firstShiftPriceSum.add(secondShiftPriceSum));
        model.addAttribute("firstShiftPaidSum", firstShiftPaidSum);
        model.addAttribute("firstShiftNotPaidSum", firstShiftPriceSum.subtract(firstShiftPaidSum));
        model.addAttribute("paidSum", firstShiftPaidSum.add(secondShiftPaidSum));
        model.addAttribute("secondShiftPaidSum", secondShiftPaidSum);
        model.addAttribute("secondShiftNotPaidSum", secondShiftPriceSum.subtract(secondShiftPaidSum));
        model.addAttribute("notPaidSum", firstShiftPriceSum.subtract(firstShiftPaidSum).add((secondShiftPriceSum.subtract(secondShiftPaidSum))));



        if (session.getAttribute("searchIsPaid") != null && session.getAttribute("searchIsPaid") != "") {
            model.addAttribute("newOrders",
                    newOrderService.findNewOrderByIsPaid(Boolean.valueOf(String.valueOf(session.getAttribute("searchIsPaid")))));
            status.setComplete();
            if (!newOrderService.findAll().isEmpty()) {
                model.addAttribute("period", newOrderService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", newOrderService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }

            return "/admin/admin-new-order-list";
        }

        if (session.getAttribute("searchDepartmentId") != null && session.getAttribute("searchDepartmentId") != "" && (Integer) session.getAttribute("searchDepartmentId") != 0) {
            List<User> users = userService.findUsersByDepartment(departmentService.getDepartmentById((Integer) session.getAttribute("searchDepartmentId")));
            List<NewOrder> newOrders = new ArrayList<>();
            for (User user : users) {
                if (newOrderService.getNewOrderByUserId(user.getUserId()) != null) {
                    newOrders.add(newOrderService.getNewOrderByUserId(user.getUserId()));
                }
            }
            status.setComplete();
            model.addAttribute("newOrders", newOrders);
            if (!newMenuService.findAll().isEmpty()) {
                model.addAttribute("period", newOrderService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", newOrderService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }

            return "/admin/admin-new-order-list";
        }

        if (session.getAttribute("searchUsername") != null && session.getAttribute("searchUsername") != "") {
            List<NewOrder> all = newOrderService.findAll();
            List<NewOrder> newOrders = new ArrayList<>();
            for (NewOrder newOrder : all) {
                if (newOrder.getUser().getUsername().contains((String) session.getAttribute("searchUsername"))) {
                    newOrders.add(newOrder);
                }
            }

            status.setComplete();
            model.addAttribute("newOrders", Optional.ofNullable(newOrders).orElse(new ArrayList<>()));
            model.addAttribute("searchUsername", null);
            if (!newOrderService.findAll().isEmpty()) {
                model.addAttribute("period", newOrderService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", newOrderService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }

            return "/admin/admin-new-order-list";
        }

        List<NewOrder> newOrders = newOrderService.findAll();
        Collections.reverse(newOrders);
        model.addAttribute("newOrders", newOrders);
        if (!newOrderService.findAll().isEmpty()) {
            model.addAttribute("period", newOrderService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newOrderService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }
        return "/admin/admin-new-order-list";
    }


    @PostMapping("/admin/newOrder/searchIsPaid")
    public String searchIdPaid(@RequestParam String searchIsPaid, Model model) {
        model.addAttribute("companyData", companyData);

        model.addAttribute("searchIsPaid", searchIsPaid);
        return "redirect:/admin/newOrder/list";
    }

    @PostMapping("/admin/newOrder/searchDepartment")
    public String searchDepartment(@RequestParam Integer searchDepartmentId, Model model) {
        model.addAttribute("searchDepartmentId", searchDepartmentId);
        model.addAttribute("companyData", companyData);

        return "redirect:/admin/newOrder/list";
    }

    @PostMapping("/admin/newOrder/searchUsername")
    public String searchLogin(@RequestParam String searchUsername, Model model) {
        model.addAttribute("searchUsername", searchUsername);
        model.addAttribute("companyData", companyData);

        return "redirect:/admin/newOrder/list";
    }

    @PostMapping("/admin/newOrder/list/paid")
    public String orderListPaidButtonForm(@RequestParam Boolean paid,
                                          @RequestParam Integer userIdUpdate) {
        NewOrder newOrder = newOrderService.getNewOrderByUserId(userIdUpdate);
        newOrder.setIsPaid(paid);
        newOrderService.save(newOrder);
        if (paid) {
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - Opłacono zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
            emailDetails.setRecipient(newOrder.getUser().getEmail());
            emailDetails.setMsgBody("Zamówienie zostało opłacone dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
            String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
//            Optional.ofNullable(user.getEmail()).orElse("")
        } else {
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - Wpłata za zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ) została zwrócona.");
            emailDetails.setRecipient(newOrder.getUser().getEmail());
            emailDetails.setMsgBody("Wpłata za zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ) została zwrócona.");
            String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);

        }
        return "redirect:/admin/newOrder/list";
    }

    @GetMapping("/admin/newOrder/list/settlement")
    public String newOrderSettlementForm(@RequestParam Integer newOrderUserId, Model model) {
        User user = userService.getUserById(newOrderUserId);
        model.addAttribute("companyData", companyData);
        model.addAttribute("user", user);
        model.addAttribute("credit", user.getCredit());
        model.addAttribute("newOrderByUserId", newOrderService.getNewOrderByUserId(newOrderUserId));
        return "/admin/admin-new-order-settlement";
    }

    @Transactional
    @PostMapping("/admin/newOrder/list/settlement")
    public String newOrderSettlement(@RequestParam Integer newOrderUserId,
                                     @RequestParam String settlementOperation, Model model) {
        User user = userService.getUserById(newOrderUserId);
        NewOrder newOrderByUserId = newOrderService.getNewOrderByUserId(newOrderUserId);
        BigDecimal creditBeforePayment = user.getCredit();
        BigDecimal toPay = newOrderByUserId.getToPay();

        if (settlementOperation.equals("subtract") && creditBeforePayment.compareTo(toPay) >= 0) {
            BigDecimal creditAfterPayment = creditBeforePayment.subtract(toPay);
            userService.updateUserMoney(creditAfterPayment, user.getUserId());
            newOrderByUserId.setIsPaid(true);
            newOrderService.save(newOrderByUserId);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - Rozliczono zamówienie dla " + newOrderByUserId.getUser().getUsername() + " " + newOrderByUserId.getPeriod() + " ( " + newOrderByUserId.getPeriodDate() + " ).");
            emailDetails.setRecipient(newOrderByUserId.getUser().getEmail());
            emailDetails.setMsgBody("Zamówienie zostało rozliczone z konta dla " + newOrderByUserId.getUser().getUsername() + " " + newOrderByUserId.getPeriod() + " ( " + newOrderByUserId.getPeriodDate() + " )." +
                    " Kwota przed rozliczeniem: " + creditBeforePayment + " zł. " +
                    "Kwota rozliczenia: " + toPay + " zł. " +
                    "Stan konta po rozliczeniu : " + creditAfterPayment + " zł. ");
            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);

            emailDetails.setSubject("Obiady - Rozliczono zamówienie dla " + newOrderByUserId.getUser().getUsername() + " " + newOrderByUserId.getPeriod() + " ( " + newOrderByUserId.getPeriodDate() + " ).");
            emailDetails.setRecipient(newOrderByUserId.getUser().getEmail());
            emailDetails.setMsgBody("Zamówienie zostało rozliczone z konta dla " + newOrderByUserId.getUser().getUsername() + " " + newOrderByUserId.getPeriod() + " ( " + newOrderByUserId.getPeriodDate() + " )." +
                    " Kwota przed rozliczeniem: " + creditBeforePayment + " zł. " +
                    "Kwota rozliczenia: " + toPay + " zł. " +
                    "Stan konta po rozliczeniu : " + creditAfterPayment + " zł. ");
            model.addAttribute("userId", newOrderUserId);
            return "redirect:/admin/financialAccount/settlemnt/confirmation";
        }
        if (settlementOperation.equals("add")) {
            BigDecimal creditAfterPayment = creditBeforePayment.add(toPay);
            userService.updateUserMoney(creditAfterPayment, user.getUserId());
            newOrderByUserId.setIsPaid(false);
            newOrderService.save(newOrderByUserId);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - Anulowano rozliczonie zamówienia dla " + newOrderByUserId.getUser().getUsername() + " " + newOrderByUserId.getPeriod() + " ( " + newOrderByUserId.getPeriodDate() + " ).");
            emailDetails.setRecipient(newOrderByUserId.getUser().getEmail());
            emailDetails.setMsgBody(
                    "Rozliczenie zostało anulowane, kwota rozliczenia trafiła na konto " + newOrderByUserId.getUser().getUsername() + " " + newOrderByUserId.getPeriod() + " ( " + newOrderByUserId.getPeriodDate() + " )." +
                            " Kwota przed rozliczeniem: " + creditBeforePayment + " zł. " +
                            "Kwota rozliczenia: " + toPay + " zł. " +
                            "Stan konta po rozliczeniu : " + creditAfterPayment + " zł. ");

            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
            model.addAttribute("userId", newOrderUserId);
            return "redirect:/admin/financialAccount/settlemnt/confirmation";
        }
        return "redirect:/admin/newOrder/list";
    }

    //TODO Zmienić obsługę usuwania zamówienia (get - POST)

    @GetMapping("/user/newOrder/delete")
    public String newOrderUserDelete(@RequestParam Long id) {
        NewOrder newOrder = newOrderService.getNewOrderById(id);
        newOrderService.delete(newOrder);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Obiady - anulowano zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        emailDetails.setRecipient(newOrder.getUser().getEmail());
        emailDetails.setMsgBody("Zamówienie zostało anulowane dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
        emailService.sendMailRecipient(emailDetails, recipients);
        return "redirect:/user/newOrder";
    }


    @GetMapping("/admin/newOrder/delete")
    public String newOrderAdminDelete(@RequestParam Long id) {
        NewOrder newOrder = newOrderService.getNewOrderById(id);
        newOrderService.delete(newOrder);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Obiady - anulowano zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        emailDetails.setMsgBody("Zamówienie zostało anulowane dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
        emailService.sendMailRecipient(emailDetails, recipients);

        return "redirect:/admin/newOrder";
    }

    @GetMapping("/admin/newOrderList/delete")
    public String newOrderListAdminDelete(@RequestParam Long id) {
        NewOrder newOrder = newOrderService.getNewOrderById(id);
        newOrderService.delete(newOrder);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Obiady - anulowano zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        emailDetails.setMsgBody("Zamówienie zostało anulowane dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ). Więcej szczegółów u osoby zajmującej się obiadami");
        String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
        emailService.sendMailRecipient(emailDetails, recipients);
        return "redirect:/admin/newOrder/list";
    }

    @GetMapping("/user/newOrder/check")
    public String NewOrderUSerCheckView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        model.addAttribute("companyData", companyData);
        model.addAttribute("newOrder", newOrderService.getNewOrderByUserId(user.getUserId()));
        model.addAttribute("period", order.getPeriod());
        model.addAttribute("periodDate", order.getPeriodDate());
        model.addAttribute("pageId", 3);
        if (order != null && !order.getIsPaid()) {
            model.addAttribute("receivables", order.getToPay());
        } else {
            model.addAttribute("receivables", 0.00);
        }
        return "/user/new-order-user-check";
    }

    @GetMapping("/admin/newOrder/check")
    public String NewOrderAdminCheckView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        model.addAttribute("companyData", companyData);
        model.addAttribute("newOrder", newOrderService.getNewOrderByUserId(user.getUserId()));
        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }

        model.addAttribute("pageId", 3);
        if (order != null && !order.getIsPaid()) {
            model.addAttribute("receivables", order.getToPay());
        } else {
            model.addAttribute("receivables", 0.00);
        }
        return "/admin/new-order-admin-check";
    }

    @GetMapping("/user/newOrder")
    public String userNewOrderView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        model.addAttribute("companyData", companyData);
        model.addAttribute("user", user);
        model.addAttribute("pageId", 3);

        if (newOrderService.getNewOrderByUserId(user.getUserId()) != null) {
            if (order != null && !order.getIsPaid()) {
                model.addAttribute("receivables", order.getToPay());
            } else {
                model.addAttribute("receivables", 0.00);
            }
            return "redirect:/user/newOrder/check";
        }

        if (configService.getConfig().getEditMode()) {
            if (order != null && !order.getIsPaid()) {
                model.addAttribute("receivables", order.getToPay());
            } else {
                model.addAttribute("receivables", 0.00);
            }
            return "/user/user-home-editmode";
        }

        BigDecimal paymentPerc = BigDecimal.valueOf(user.getDepartment().getPaymentPerc());
        List<NewMenu> menuMonday = newMenuService.findNewMenusByDayId(1);
        List<NewMenu> menuTuesday = newMenuService.findNewMenusByDayId(2);
        List<NewMenu> menuWednesday = newMenuService.findNewMenusByDayId(3);
        List<NewMenu> menuThursday = newMenuService.findNewMenusByDayId(4);
        List<NewMenu> menuFriday = newMenuService.findNewMenusByDayId(5);

        menuMonday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuTuesday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuWednesday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuThursday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuFriday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });

        String period;
        String periodDate;

        if (!newMenuService.findAll().isEmpty()) {
            period = newMenuService.findAll().get(0).getPeriod();
            periodDate = newMenuService.findAll().get(0).getPeriodDate();
        } else {
            period = "-";
            periodDate = "-";
        }
        NewOrder newOrder = getNewOrder(period, periodDate, user);
        model.addAttribute("newOrder", newOrder);
        model.addAttribute("newMenuMonday", menuMonday);
        model.addAttribute("newMenuTuesday", menuTuesday);
        model.addAttribute("newMenuWednesday", menuWednesday);
        model.addAttribute("newMenuThursday", menuThursday);
        model.addAttribute("newMenuFriday", menuFriday);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("editUserId", user.getUserId());

        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }

        if (order != null && !order.getIsPaid()) {
            model.addAttribute("receivables", order.getToPay());
        } else {
            model.addAttribute("receivables", 0.00);
        }
        return "/user/new-order-user";
    }

    @PostMapping("/user/newOrder")
    public String userNewOrder(NewOrder newOrder, Principal principal, Model model) {

        if (configService.getConfig().getEditMode()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("companyData", companyData);
            return "/user/user-new-order-not-complete";
        }

        User user = userService.getUserById(newOrder.getUser().getUserId());
        BigDecimal paymentPerc = BigDecimal.valueOf(user.getDepartment().getPaymentPerc());

        NewMenu mealMonday = newMenuService.findByMealNo(newOrder.getMealMon());
        NewMenu mealTuesday = newMenuService.findByMealNo(newOrder.getMealTue());
        NewMenu mealWednesday = newMenuService.findByMealNo(newOrder.getMealWed());
        NewMenu mealThursday = newMenuService.findByMealNo(newOrder.getMealThu());
        NewMenu mealFriday = newMenuService.findByMealNo(newOrder.getMealFri());

        newOrder.setPriceMon(mealMonday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceTue(mealTuesday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceWed(mealWednesday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceThu(mealThursday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceFri(mealFriday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));


        newOrder.setMealMonName(newMenuService.findByMealNo(newOrder.getMealMon()).getMealName());
        newOrder.setMealTueName(newMenuService.findByMealNo(newOrder.getMealTue()).getMealName());
        newOrder.setMealWedName(newMenuService.findByMealNo(newOrder.getMealWed()).getMealName());
        newOrder.setMealThuName(newMenuService.findByMealNo(newOrder.getMealThu()).getMealName());
        newOrder.setMealFriName(newMenuService.findByMealNo(newOrder.getMealFri()).getMealName());

        if (newOrder.getMealMonName().equals("Brak")) {
            newOrder.setShiftMon(0);
        }
        if (newOrder.getMealTueName().equals("Brak")) {
            newOrder.setShiftTue(0);
        }
        if (newOrder.getMealWedName().equals("Brak")) {
            newOrder.setShiftWed(0);
        }
        if (newOrder.getMealThuName().equals("Brak")) {
            newOrder.setShiftThu(0);
        }
        if (newOrder.getMealFriName().equals("Brak")) {
            newOrder.setShiftFri(0);
        }
        if (newOrder.getShiftMon() == 0 &&
                newOrder.getShiftTue() == 0 &&
                newOrder.getShiftWed() == 0 &&
                newOrder.getShiftThu() == 0 &&
                newOrder.getShiftFri() == 0) {
            model.addAttribute("mealsMonday", newMenuService.newMenuFindByDayId(1));
            model.addAttribute("mealsTuesday", newMenuService.newMenuFindByDayId(2));
            model.addAttribute("mealsWednesday", newMenuService.newMenuFindByDayId(3));
            model.addAttribute("mealsThursday", newMenuService.newMenuFindByDayId(4));
            model.addAttribute("mealsFriday", newMenuService.newMenuFindByDayId(5));
            model.addAttribute("pageId", 0);
            if (!newMenuService.findAll().isEmpty()) {
                model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }
            return "redirect:/user/home";
        }

        newOrder.setToPay(newOrder.getPriceMon().add(newOrder.getPriceTue()).add(newOrder.getPriceWed())
                .add(newOrder.getPriceThu()).add(newOrder.getPriceFri()));

        newOrderService.save(newOrder);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Obiady - zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setMsgBody(
                "Poniedziałek: " + newOrder.getMealMonName() + " " + "zmiana: " + newOrder.getShiftMon() + " ||| " +
                        "Wtorek: " + newOrder.getMealTueName() + " " + "zmiana: " + newOrder.getShiftTue() + " ||| " +
                        "Środe: " + newOrder.getMealWedName() + " " + "zmiana: " + newOrder.getShiftWed() + " ||| " +
                        "Czwartek: " + newOrder.getMealThuName() + " " + "zmiana: " + newOrder.getShiftThu() + " ||| " +
                        "Piątek: " + newOrder.getMealFriName() + " " + "zmiana: " + newOrder.getShiftFri() + " ||| " +
                        "Łączna kwota za zamówienie: " + newOrder.getToPay() + " zł.");
        String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
        emailService.sendMailRecipient(emailDetails, recipients);
        return "redirect:/user/newOrder/check";
    }

    @GetMapping("/admin/newOrder")
    public String adminNewOrderView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        model.addAttribute("companyData", companyData);
        model.addAttribute("user", user);
        model.addAttribute("pageId", 3);

        if (configService.getConfig().getEditMode()) {
            if (order != null && !order.getIsPaid()) {
                model.addAttribute("receivables", order.getToPay());
            } else {
                model.addAttribute("receivables", 0.00);
            }
            return "/admin/admin-home-editmode";
        }

        if (newOrderService.getNewOrderByUserId(user.getUserId()) != null) {
            return "redirect:/admin/newOrder/check";
        }

        BigDecimal paymentPerc = BigDecimal.valueOf(user.getDepartment().getPaymentPerc());

        List<NewMenu> menuMonday = newMenuService.findNewMenusByDayId(1);
        List<NewMenu> menuTuesday = newMenuService.findNewMenusByDayId(2);
        List<NewMenu> menuWednesday = newMenuService.findNewMenusByDayId(3);
        List<NewMenu> menuThursday = newMenuService.findNewMenusByDayId(4);
        List<NewMenu> menuFriday = newMenuService.findNewMenusByDayId(5);

        menuMonday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuTuesday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuWednesday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuThursday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuFriday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });

        String period;
        String periodDate;

        if (!newMenuService.findAll().isEmpty()) {
            period = newMenuService.findAll().get(0).getPeriod();
            periodDate = newMenuService.findAll().get(0).getPeriodDate();
        } else {
            period = "-";
            periodDate = "-";
        }
        NewOrder newOrder = getNewOrder(period, periodDate, user);
        model.addAttribute("newOrder", newOrder);
        model.addAttribute("newMenuMonday", menuMonday);
        model.addAttribute("newMenuTuesday", menuTuesday);
        model.addAttribute("newMenuWednesday", menuWednesday);
        model.addAttribute("newMenuThursday", menuThursday);
        model.addAttribute("newMenuFriday", menuFriday);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("editUserId", user.getUserId());
        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }
//        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        if (order != null && !order.getIsPaid()) {
            model.addAttribute("receivables", order.getToPay());
        } else {
            model.addAttribute("receivables", 0.00);
        }
        return "/admin/new-order-admin";
    }

    @PostMapping("/admin/newOrder")
    public String adminNewOrder(NewOrder newOrder, Model model) {

        model.addAttribute("companyData", companyData);
        User user = userService.getUserById(newOrder.getUser().getUserId());
        BigDecimal paymentPerc = BigDecimal.valueOf(user.getDepartment().getPaymentPerc());

        NewMenu mealMonday = newMenuService.findByMealNo(newOrder.getMealMon());
        NewMenu mealTuesday = newMenuService.findByMealNo(newOrder.getMealTue());
        NewMenu mealWednesday = newMenuService.findByMealNo(newOrder.getMealWed());
        NewMenu mealThursday = newMenuService.findByMealNo(newOrder.getMealThu());
        NewMenu mealFriday = newMenuService.findByMealNo(newOrder.getMealFri());

        newOrder.setPriceMon(mealMonday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceTue(mealTuesday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceWed(mealWednesday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceThu(mealThursday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceFri(mealFriday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));

        newOrder.setMealMonName(newMenuService.findByMealNo(newOrder.getMealMon()).getMealName());
        newOrder.setMealTueName(newMenuService.findByMealNo(newOrder.getMealTue()).getMealName());
        newOrder.setMealWedName(newMenuService.findByMealNo(newOrder.getMealWed()).getMealName());
        newOrder.setMealThuName(newMenuService.findByMealNo(newOrder.getMealThu()).getMealName());
        newOrder.setMealFriName(newMenuService.findByMealNo(newOrder.getMealFri()).getMealName());

        if (newOrder.getMealMonName().equals("Brak")) {
            newOrder.setShiftMon(0);
        }
        if (newOrder.getMealTueName().equals("Brak")) {
            newOrder.setShiftTue(0);
        }
        if (newOrder.getMealWedName().equals("Brak")) {
            newOrder.setShiftWed(0);
        }
        if (newOrder.getMealThuName().equals("Brak")) {
            newOrder.setShiftThu(0);
        }
        if (newOrder.getMealFriName().equals("Brak")) {
            newOrder.setShiftFri(0);
        }
        if (newOrder.getShiftMon() == 0 &&
                newOrder.getShiftTue() == 0 &&
                newOrder.getShiftWed() == 0 &&
                newOrder.getShiftThu() == 0 &&
                newOrder.getShiftFri() == 0) {
            model.addAttribute("mealsMonday", newMenuService.newMenuFindByDayId(1));
            model.addAttribute("mealsTuesday", newMenuService.newMenuFindByDayId(2));
            model.addAttribute("mealsWednesday", newMenuService.newMenuFindByDayId(3));
            model.addAttribute("mealsThursday", newMenuService.newMenuFindByDayId(4));
            model.addAttribute("mealsFriday", newMenuService.newMenuFindByDayId(5));
            model.addAttribute("pageId", 0);
            if (!newMenuService.findAll().isEmpty()) {
                model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }
            return "redirect:/admin/home";
        }

        newOrder.setToPay(newOrder.getPriceMon().add(newOrder.getPriceTue()).add(newOrder.getPriceWed())
                .add(newOrder.getPriceThu()).add(newOrder.getPriceFri()));

        newOrderService.save(newOrder);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Obiady - zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setMsgBody(
                "Poniedziałek: " + newOrder.getMealMonName() + " " + "zmiana: " + newOrder.getShiftMon() + " ||| " +
                        "Wtorek: " + newOrder.getMealTueName() + " " + "zmiana: " + newOrder.getShiftTue() + " ||| " +
                        "Środe: " + newOrder.getMealWedName() + " " + "zmiana: " + newOrder.getShiftWed() + " ||| " +
                        "Czwartek: " + newOrder.getMealThuName() + " " + "zmiana: " + newOrder.getShiftThu() + " ||| " +
                        "Piątek: " + newOrder.getMealFriName() + " " + "zmiana: " + newOrder.getShiftFri() + " ||| " +
                        "Łączna kwota za zamówienie: " + newOrder.getToPay() + " zł.");
        String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
        emailService.sendMailRecipient(emailDetails, recipients);
        return "redirect:/admin/newOrder/check";
    }

    @GetMapping("/admin/newOrder/orderForUser")
    public String newOrderForUserView(@RequestParam(value = "orderUserId") Integer orderUserId, Model model) {
        User user = userService.getUserById(orderUserId);
        NewOrder order = newOrderService.getNewOrderByUserId(user.getUserId());
        if (order != null && !order.getIsPaid()) {
            model.addAttribute("receivables", order.getToPay());
        } else {
            model.addAttribute("receivables", 0.00);
        }
        model.addAttribute("user", user);

        if (newOrderService.getNewOrderByUserId(user.getUserId()) != null) {
            return "redirect:/admin/newOrder/list";
        }

        BigDecimal paymentPerc = BigDecimal.valueOf(user.getDepartment().getPaymentPerc());

        List<NewMenu> menuMonday = newMenuService.findNewMenusByDayId(1);
        List<NewMenu> menuTuesday = newMenuService.findNewMenusByDayId(2);
        List<NewMenu> menuWednesday = newMenuService.findNewMenusByDayId(3);
        List<NewMenu> menuThursday = newMenuService.findNewMenusByDayId(4);
        List<NewMenu> menuFriday = newMenuService.findNewMenusByDayId(5);

        menuMonday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuTuesday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuWednesday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuThursday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });
        menuFriday
                .forEach(e -> {
                    e.setMealPrice(e.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
                    e.setMealName(e.getMealName().concat(" ").concat(String.valueOf(e.getMealPrice())).concat(" zł"));
                });

        String period;
        String periodDate;
        if (!newMenuService.findAll().isEmpty()) {
            period = newMenuService.findAll().get(0).getPeriod();
            periodDate = newMenuService.findAll().get(0).getPeriodDate();
        } else {
            period = "-";
            periodDate = "-";
        }
        NewOrder newOrder = getNewOrder(period, periodDate, user);
        model.addAttribute("newOrder", newOrder);
        model.addAttribute("newMenuMonday", menuMonday);
        model.addAttribute("newMenuTuesday", menuTuesday);
        model.addAttribute("newMenuWednesday", menuWednesday);
        model.addAttribute("newMenuThursday", menuThursday);
        model.addAttribute("newMenuFriday", menuFriday);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("companyData", companyData);
        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }

        return "/admin/new-order-admin-for-user";
    }

    @PostMapping("/admin/newOrder/orderForUser")
    public String adminNewOrderForUser(NewOrder newOrder, Model model) {
        model.addAttribute("companyData", companyData);


        User user = userService.getUserById(newOrder.getUser().getUserId());
        BigDecimal paymentPerc = BigDecimal.valueOf(user.getDepartment().getPaymentPerc());

        NewMenu mealMonday = newMenuService.findByMealNo(newOrder.getMealMon());
        NewMenu mealTuesday = newMenuService.findByMealNo(newOrder.getMealTue());
        NewMenu mealWednesday = newMenuService.findByMealNo(newOrder.getMealWed());
        NewMenu mealThursday = newMenuService.findByMealNo(newOrder.getMealThu());
        NewMenu mealFriday = newMenuService.findByMealNo(newOrder.getMealFri());

        newOrder.setPriceMon(mealMonday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceTue(mealTuesday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceWed(mealWednesday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceThu(mealThursday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));
        newOrder.setPriceFri(mealFriday.getMealPrice().multiply(paymentPerc).divide(BigDecimal.valueOf(100)));


        newOrder.setMealMonName(newMenuService.findByMealNo(newOrder.getMealMon()).getMealName());
        newOrder.setMealTueName(newMenuService.findByMealNo(newOrder.getMealTue()).getMealName());
        newOrder.setMealWedName(newMenuService.findByMealNo(newOrder.getMealWed()).getMealName());
        newOrder.setMealThuName(newMenuService.findByMealNo(newOrder.getMealThu()).getMealName());
        newOrder.setMealFriName(newMenuService.findByMealNo(newOrder.getMealFri()).getMealName());
        if (newOrder.getMealMonName().equals("Brak")) {
            newOrder.setShiftMon(0);
        }
        if (newOrder.getMealTueName().equals("Brak")) {
            newOrder.setShiftTue(0);
        }
        if (newOrder.getMealWedName().equals("Brak")) {
            newOrder.setShiftWed(0);
        }
        if (newOrder.getMealThuName().equals("Brak")) {
            newOrder.setShiftThu(0);
        }
        if (newOrder.getMealFriName().equals("Brak")) {
            newOrder.setShiftFri(0);
        }
        if (newOrder.getShiftMon() == 0 &&
                newOrder.getShiftTue() == 0 &&
                newOrder.getShiftWed() == 0 &&
                newOrder.getShiftThu() == 0 &&
                newOrder.getShiftFri() == 0) {
            model.addAttribute("mealsMonday", newMenuService.newMenuFindByDayId(1));
            model.addAttribute("mealsTuesday", newMenuService.newMenuFindByDayId(2));
            model.addAttribute("mealsWednesday", newMenuService.newMenuFindByDayId(3));
            model.addAttribute("mealsThursday", newMenuService.newMenuFindByDayId(4));
            model.addAttribute("mealsFriday", newMenuService.newMenuFindByDayId(5));
            model.addAttribute("pageId", 0);
            if (!newMenuService.findAll().isEmpty()) {
                model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
                model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
            } else {
                model.addAttribute("period", "-");
                model.addAttribute("periodDate", "-");
            }
            return "redirect:/admin/newOrder/list";
        }

        newOrder.setToPay(newOrder.getPriceMon().add(newOrder.getPriceTue()).add(newOrder.getPriceWed())
                .add(newOrder.getPriceThu()).add(newOrder.getPriceFri()));


        newOrderService.save(newOrder);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Obiady - zamówienie dla " + newOrder.getUser().getUsername() + " " + newOrder.getPeriod() + " ( " + newOrder.getPeriodDate() + " ).");
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setMsgBody(
                "Poniedziałek: " + newOrder.getMealMonName() + " " + "zmiana: " + newOrder.getShiftMon() + " ||| " +
                        "Wtorek: " + newOrder.getMealTueName() + " " + "zmiana: " + newOrder.getShiftTue() + " ||| " +
                        "Środa: " + newOrder.getMealWedName() + " " + "zmiana: " + newOrder.getShiftWed() + " ||| " +
                        "Czwartek: " + newOrder.getMealThuName() + " " + "zmiana: " + newOrder.getShiftThu() + " ||| " +
                        "Piątek: " + newOrder.getMealFriName() + " " + "zmiana: " + newOrder.getShiftFri() + " ||| " +
                        "Łączna kwota za zamówienie: " + newOrder.getToPay() + " zł.");
        String[] recipients = {Optional.ofNullable(newOrder.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
        emailService.sendMailRecipient(emailDetails, recipients);
        return "redirect:/admin/newOrder/list";
    }

    @PostMapping("/admin/newOrder/clear")
    public String newOrderClear() {
        Config config = configService.getConfig();
        config.setEditMode(true);
        configService.save(config);
        newOrderService.deleteAll();
        newMenuService.deleteAll();
        return "redirect:/admin/financial";
    }
}
