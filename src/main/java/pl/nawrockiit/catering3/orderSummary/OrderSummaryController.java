package pl.nawrockiit.catering3.orderSummary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.nawrockiit.catering3.actualOrder.ActualOrder;
import pl.nawrockiit.catering3.actualOrder.ActualOrderService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller

@RequestMapping("/admin")
public class OrderSummaryController {

    private final Map<String, String> companyData;
    private final OrderSummaryService orderSummaryService;
    private final ActualOrderService actualOrderService;

    public OrderSummaryController(@Value("#{${companyData}}") Map<String, String> companyData, OrderSummaryService orderSummaryService, ActualOrderService actualOrderService) {
        this.companyData = companyData;
        this.orderSummaryService = orderSummaryService;
        this.actualOrderService = actualOrderService;
    }

    @GetMapping("/orderSummary")
    public String orderSummaryView(@RequestParam(required = false) Integer shift, Model model, HttpSession session) {
        List<OrderSummary> orderSummaryList = orderSummaryService.findAll();
        List<ActualOrder> actualOrderMeals = actualOrderService.findAll();
        List<Integer> mealsQtyPerDayFirstShift = new ArrayList<>();
        List<Integer> mealsQtyPerDaySecondShift = new ArrayList<>();


        BigDecimal firstShiftPriceSum = BigDecimal.valueOf(0);
        BigDecimal secondShiftPriceSum = BigDecimal.valueOf(0);
        for (ActualOrder actualOrderMeal : actualOrderMeals) {
            if (actualOrderMeal.getShiftMon() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceMon());
            } else if (actualOrderMeal.getShiftMon() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceMon());
            }
            if (actualOrderMeal.getShiftTue() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceTue());
            } else if (actualOrderMeal.getShiftTue() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceTue());
            }
            if (actualOrderMeal.getShiftWed() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceWed());
            } else if (actualOrderMeal.getShiftWed() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceWed());
            }
            if (actualOrderMeal.getShiftThu() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceThu());
            } else if (actualOrderMeal.getShiftThu() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceThu());
            }
            if (actualOrderMeal.getShiftFri() == 1) {
                firstShiftPriceSum = firstShiftPriceSum.add(actualOrderMeal.getPriceFri());
            } else if (actualOrderMeal.getShiftFri() == 2) {
                secondShiftPriceSum = secondShiftPriceSum.add(actualOrderMeal.getPriceFri());
            }
        }


        for (int i = 1; i <= 5; i++) {
            Integer sumPerDayFirstShift = 0;
            Integer sumPerDaySecondShift = 0;
            for (OrderSummary orderSummary : orderSummaryService.findOrderSummaryByDayId(i)) {
                if (!orderSummary.getMealName().equals("Brak")) {
                    sumPerDayFirstShift = sumPerDayFirstShift + orderSummary.getFirstShiftQuantity();
                    sumPerDaySecondShift = sumPerDaySecondShift + orderSummary.getSecondShiftQuantity();
                }
            }
            mealsQtyPerDayFirstShift.add(sumPerDayFirstShift);
            mealsQtyPerDaySecondShift.add(sumPerDaySecondShift);
        }
        model.addAttribute("mealsQtyPerDayFirstShift", mealsQtyPerDayFirstShift);
        model.addAttribute("mealsQtyPerDaySecondShift", mealsQtyPerDaySecondShift);
        model.addAttribute("orderSummaryList", orderSummaryList);
        model.addAttribute("shift", shift);
        model.addAttribute("pageId", 12);
        model.addAttribute("companyData", companyData);

        if (!orderSummaryService.findAll().isEmpty()) {
            model.addAttribute("period", orderSummaryService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", orderSummaryService.findAll().get(0).getPeriodDate());
        } else {
            model.addAttribute("period", "-");
            model.addAttribute("periodDate", "-");
        }
        if (shift == null || shift == 1) {
            model.addAttribute("firstShiftPriceSum", firstShiftPriceSum);
            return "/admin/admin-dinner-ids-1shift";
        } else {
            model.addAttribute("secondShiftPriceSum", secondShiftPriceSum);
            return "/admin/admin-dinner-ids-2shift";
        }
    }



//    int year = 2024; // Rok
//        int weekNumber = 2; // Numer tygodnia
//
//        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
//
//        // Dodaj odpowiednią liczbę tygodni do pierwszego dnia roku
//        LocalDate desiredDate = firstDayOfYear.plusWeeks(weekNumber - 1);
//
//        // Ustaw na pierwszy dzień tygodnia
//        LocalDate firstDayOfWeek = desiredDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//
//        // Ustaw na ostatni dzień tygodnia
//        LocalDate lastDayOfWeek = desiredDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
//
//        System.out.println("Data na podstawie numeru tygodnia " + weekNumber + " w roku " + year + ":");
//        System.out.println("Pierwszy dzień tygodnia: " + firstDayOfWeek);
//        System.out.println("Ostatni dzień tygodnia: " + lastDayOfWeek);
//
}
