package pl.nawrockiit.catering3.financial;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.actualOrder.ActualOrderService;
import pl.nawrockiit.catering3.config.Config;
import pl.nawrockiit.catering3.config.ConfigService;
import pl.nawrockiit.catering3.department.Department;
import pl.nawrockiit.catering3.department.DepartmentService;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.newOrder.NewOrderService;
import pl.nawrockiit.catering3.orderSummary.OrderSummaryService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class FinancialServiceimpl implements FinancialService{

    private final DepartmentService departmentService;
    private final UserService userService;
    private final NewOrderService newOrderService;
    private final NewMenuService newMenuService;
    private final ConfigService configService;
    private final OrderSummaryService orderSummaryService;
    private final ActualOrderService actualOrderService;


    @Override
    public List<FinancialDepartmentSummary> getfinancialDepartmentSummaryList() {

        List<FinancialDepartmentSummary> financialDepartmentSummaryList = new ArrayList<>();
        List<Department> allDepartments = departmentService.findAll();

        for (Department department : allDepartments) {
            FinancialDepartmentSummary financialDepartmentSummary = new FinancialDepartmentSummary();
            financialDepartmentSummary.setDepartmentName(department.getName());
            List<Integer> usersId = new ArrayList<>();
            List<User> usersByDepartment = userService.findUsersByDepartment(department);

            for (User userBelongToDepartment : usersByDepartment) {
                usersId.add(userBelongToDepartment.getUserId());
            }
            for (Integer id : usersId) {
                NewOrder newOrder = newOrderService.getNewOrderByUserId(id);
                if (newOrder != null) {
                    if (!newOrder.getIsPaid()) {
                        financialDepartmentSummary.setNotPaidOrders(financialDepartmentSummary.getNotPaidOrders() + 1);
                    }
                    financialDepartmentSummary.setDepartmentSummaryDiscountPrice(financialDepartmentSummary.getDepartmentSummaryDiscountPrice().add(newOrder.getToPay()));
                    if (newMenuService.newMenuListNotEmpty()) {
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealMon()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealTue()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealWed()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealThu()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealFri()).getMealPrice()));
                    } else {
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(BigDecimal.valueOf(0));
                    }
                }
            }
            financialDepartmentSummaryList.add(financialDepartmentSummary);
        }
        return financialDepartmentSummaryList;
    }

    public BigDecimal getSumOfDepartmentDiscountPrice() {
        List<Department> allDepartments = departmentService.findAll();
        BigDecimal sumOfDepartmentDiscountPrice = new BigDecimal(0);
        for (Department department : allDepartments) {
            FinancialDepartmentSummary financialDepartmentSummary = new FinancialDepartmentSummary();
            financialDepartmentSummary.setDepartmentName(department.getName());
            List<Integer> usersId = new ArrayList<>();
            List<User> usersByDepartment = userService.findUsersByDepartment(department);
            for (User userBelongToDepartment : usersByDepartment) {
                usersId.add(userBelongToDepartment.getUserId());
            }
            for (Integer id : usersId) {
                NewOrder newOrder = newOrderService.getNewOrderByUserId(id);
                if (newOrder != null) {
                    if (!newOrder.getIsPaid()) {
                        financialDepartmentSummary.setNotPaidOrders(financialDepartmentSummary.getNotPaidOrders() + 1);
                    }
                    financialDepartmentSummary.setDepartmentSummaryDiscountPrice(financialDepartmentSummary.getDepartmentSummaryDiscountPrice().add(newOrder.getToPay()));
                }
            }
            sumOfDepartmentDiscountPrice = sumOfDepartmentDiscountPrice.add(financialDepartmentSummary.getDepartmentSummaryDiscountPrice());
        }
        return sumOfDepartmentDiscountPrice;
    }

    public BigDecimal getSumOfDepartmentFullPrice() {
        List<Department> allDepartments = departmentService.findAll();
        BigDecimal sumOfDepartmentFullPrice = new BigDecimal(0);
        for (Department department : allDepartments) {
            FinancialDepartmentSummary financialDepartmentSummary = new FinancialDepartmentSummary();
            financialDepartmentSummary.setDepartmentName(department.getName());
            List<Integer> usersId = new ArrayList<>();
            List<User> usersByDepartment = userService.findUsersByDepartment(department);
            for (User userBelongToDepartment : usersByDepartment) {
                usersId.add(userBelongToDepartment.getUserId());
            }
            for (Integer id : usersId) {
                NewOrder newOrder = newOrderService.getNewOrderByUserId(id);
                if (newOrder != null) {
                    if (!newOrder.getIsPaid()) {
                        financialDepartmentSummary.setNotPaidOrders(financialDepartmentSummary.getNotPaidOrders() + 1);
                    }
                    if (newMenuService.newMenuListNotEmpty()) {
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealMon()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealTue()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealWed()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealThu()).getMealPrice()));
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(financialDepartmentSummary.getDepartmentSummaryFullPrice().add(newMenuService.findByMealNo(newOrder.getMealFri()).getMealPrice()));
                    } else {
                        financialDepartmentSummary.setDepartmentSummaryFullPrice(BigDecimal.valueOf(0));
                    }
                }
            }
            sumOfDepartmentFullPrice = sumOfDepartmentFullPrice.add(financialDepartmentSummary.getDepartmentSummaryFullPrice());
        }
        return sumOfDepartmentFullPrice;
    }

    @Transactional
    @Override
    public void closeWeekRewriteOrdersAndNewMenu() {
        Config config = configService.getConfig();
        config.setEditMode(true);
        configService.save(config);
        orderSummaryService.deleteAll();
        actualOrderService.deleteAll();
        orderSummaryService.copyNewOrdersToActualOrders();
        orderSummaryService.collectOrderSummary();
        newOrderService.deleteAll();
        newMenuService.deleteAll();
    }
}
