package pl.nawrockiit.catering3.financial;

import lombok.AllArgsConstructor;
import org.apache.catalina.Session;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.nawrockiit.catering3.config.Config;
import pl.nawrockiit.catering3.config.ConfigService;
import pl.nawrockiit.catering3.email.EmailDetails;
import pl.nawrockiit.catering3.email.EmailService;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.newOrder.NewOrderService;
import pl.nawrockiit.catering3.orderSummary.OrderSummary;
import pl.nawrockiit.catering3.orderSummary.OrderSummaryService;
import pl.nawrockiit.catering3.price.Price;
import pl.nawrockiit.catering3.price.PriceService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;
import pl.nawrockiit.catering3.xlsx.ExcelExportService;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@SessionAttributes({"userId", "msg"})
public class FinancialController {

    private final Map<String, String> companyData;
    private final NewOrderService newOrderService;
    private final ConfigService configService;
    private final FinancialService financialService;
    private final EmailService emailService;
    private final ExcelExportService excelExportService;
    private final NewMenuService newMenuService;
    private final UserService userService;
    private final PriceService priceService;
    private final OrderSummaryService orderSummaryService;

    public FinancialController(@Value("#{${companyData}}") Map<String, String> companyData, NewOrderService newOrderService, ConfigService configService, FinancialService financialService, EmailService emailService, ExcelExportService excelExportService, NewMenuService newMenuService, UserService userService, PriceService priceService, OrderSummaryService orderSummaryService) {
        this.companyData = companyData;
        this.newOrderService = newOrderService;
        this.configService = configService;
        this.financialService = financialService;
        this.emailService = emailService;
        this.excelExportService = excelExportService;
        this.newMenuService = newMenuService;
        this.userService = userService;
        this.priceService = priceService;
        this.orderSummaryService = orderSummaryService;
    }

    @GetMapping("/financial")
    public String financialSummary(Model model) {
        model.addAttribute("companyData", companyData);
        model.addAttribute("financialDepartmentSummaryList", financialService.getfinancialDepartmentSummaryList());
        model.addAttribute("sumOfDepartmentDiscountPrice", financialService.getSumOfDepartmentDiscountPrice());
        model.addAttribute("sumOfDepartmentFullPrice", financialService.getSumOfDepartmentFullPrice());
        model.addAttribute("refundation", financialService.getSumOfDepartmentFullPrice().subtract(financialService.getSumOfDepartmentDiscountPrice()));
        if (!newMenuService.findAll().isEmpty()) {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }
        model.addAttribute("pageId", 11);

        Config configValues = configService.getConfig();
        model.addAttribute(configValues);
        return "/admin/admin-financial";
    }

    @GetMapping("/financial/closeWeek")
    public String closeWeekForm(Model model) {
        if (newOrderService.findAll().isEmpty()){
            model.addAttribute("companyData", companyData);
            return "/admin/close-week-2nd-conf";
        }
        model.addAttribute("pageId", 11);
        model.addAttribute("companyData", companyData);
        return "/admin/close-week";
    }

    @PostMapping("/financial/closeWeek")
    public String closeWeek(@RequestParam(required = false) Boolean confirm, Model model) {

        if (!Optional.ofNullable(confirm).orElse(false)){
//        if (!confirm){
            model.addAttribute("companyData", companyData);
            return "/admin/close-week-2nd-conf";
        }

        financialService.closeWeekRewriteOrdersAndNewMenu();
        model.addAttribute("companyData", companyData);
        model.addAttribute("financialDepartmentSummaryList", financialService.getfinancialDepartmentSummaryList());
        model.addAttribute("sumOfDepartmentDiscountPrice", financialService.getSumOfDepartmentDiscountPrice());
        model.addAttribute("sumOfDepartmentFullPrice", financialService.getSumOfDepartmentFullPrice());
        model.addAttribute("refundation", financialService.getSumOfDepartmentFullPrice().subtract(financialService.getSumOfDepartmentDiscountPrice()));
        return "redirect:/admin/financial";
    }

    @PostMapping("/financial/reminderNotPaid")
    public String reminderNotPaid() {
        List<NewOrder> newOrderByIsNotPaid = newOrderService.findNewOrderByIsPaid(false);
        for (NewOrder newOrderNotPaid : newOrderByIsNotPaid) {
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - przypomnienie o nieopłaconych obiadach dla " + newOrderNotPaid.getUser().getUsername() + " " + newOrderNotPaid.getPeriod() + " ( " + newOrderNotPaid.getPeriodDate() + " ).");
            emailDetails.setMsgBody("Przypominamy o opłaceniu zamówionych obiadów dla konta " + newOrderNotPaid.getUser().getUsername() + " " + newOrderNotPaid.getPeriod() + " ( " + newOrderNotPaid.getPeriodDate() + " ).");
            String[] recipients = {Optional.ofNullable(newOrderNotPaid.getUser().getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
        }
//        Optional.ofNullable(user.getEmail()).orElse("")
        return "redirect:/admin/financial";
    }

    @PostMapping("/getOrderSummaryInXls")
    public void getOrderSummaryInXls(HttpServletResponse servletResponse) throws IOException {
        excelExportService.getOrderSummeryInXls(servletResponse);
    }
    @PostMapping("/getDataBeforeCloseWeekInXl")
    public void getDataBeforeCloseWeekInXl(HttpServletResponse servletResponse) throws IOException {
        excelExportService. getDataBeforeCloseWeekInXls(servletResponse);
    }

    @PostMapping("/getDataAfterCloseWeekInXl")
    public void getDataAfterCloseWeekInXl(HttpServletResponse servletResponse) throws IOException {
        excelExportService. getDataAfterCloseWeekInXls(servletResponse);
    }

    @GetMapping("/financialAccount")
    public String financialAccountView(@RequestParam Integer userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("companyData", companyData);
        return "admin/admin-financial-account";
    }

    @Transactional
    @PostMapping("/financialAccount")
    public String financialAccount(@RequestParam Integer userId,
                                   @RequestParam BigDecimal amount,
                                   @RequestParam String moneyOperation,
                                   Model model) {

        User user = userService.getUserById(userId);
        BigDecimal creditBeforeOperation = user.getCredit();
        if (moneyOperation.equals("plus") && amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            BigDecimal creditAfterOperation = creditBeforeOperation.add(amount);
            userService.updateUserMoney(creditAfterOperation, userId);

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - Doładowano konto dla " + user.getUsername() + " kwotą " + amount + " zł.");
            emailDetails.setMsgBody("Konto " + user.getUsername() +
                    " zostało doładowane kwotą: " + amount + " zł. " +
                    "Stan konta przed doladowaniem: " + creditBeforeOperation + " zł. " +
                    "Stan konta po doładowaniu : " + creditAfterOperation + " zł.");

            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
        }
        if (moneyOperation.equals("minus") && creditBeforeOperation.compareTo(amount) >= 0 && amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            BigDecimal creditAfterOperation = creditBeforeOperation.subtract(amount);
            userService.updateUserMoney(creditAfterOperation, userId);

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Obiady - Wypłacono z konta " + user.getUsername() + " kwotę " + amount + " zł.");
            emailDetails.setMsgBody("Z konta " + user.getUsername() +
                    " została wypłacona kwota: : " + amount + " zł. " +
                    "Stan konta przed wypłatą: " + creditBeforeOperation + " zł. " +
                    "Stan konta po wypłacie : " + creditAfterOperation + " zł.");
            String[] recipients = {Optional.ofNullable(user.getEmail()).orElse(""), companyData.get("companyEmail")};
            emailService.sendMailRecipient(emailDetails, recipients);
        } else if (moneyOperation.equals("minus") && creditBeforeOperation.compareTo(amount) < 0 && amount.compareTo(BigDecimal.valueOf(0)) > 0){
            model.addAttribute("msg", "Podana wartość przekracza ilość dostępnych środków. " +
                    "Nie odjęto środków");
            model.addAttribute("userId", userId);
            return "redirect:/admin/financialAccount/confirmation";
        }
        model.addAttribute("msg", "");
        model.addAttribute("userId", userId);
        return "redirect:/admin/financialAccount/confirmation";
    }

    @GetMapping("/financialAccount/confirmation")
    public String financialAccountConfirmation(HttpSession session, Model model) {
        User user = userService.getUserById((Integer) session.getAttribute("userId"));
        String msg = (String) session.getAttribute("msg");
        model.addAttribute("user", user);
        model.addAttribute("msg", msg);
        model.addAttribute("companyData", companyData);
        return "/admin/admin-financial-confirmation";
    }

    @GetMapping("/financialAccount/settlemnt/confirmation")
    public String financialAccountTransactionConfirmation(HttpSession session, Model model) {
        User user = userService.getUserById((Integer) session.getAttribute("userId"));
        String msg = (String) session.getAttribute("msg");
        model.addAttribute("user", user);
//        model.addAttribute("msg", msg);
        model.addAttribute("companyData", companyData);
        return "/admin/admin-financial-transaction-confirmation";
    }
}
