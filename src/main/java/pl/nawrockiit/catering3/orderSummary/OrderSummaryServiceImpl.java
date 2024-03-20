package pl.nawrockiit.catering3.orderSummary;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.actualOrder.ActualOrder;
import pl.nawrockiit.catering3.actualOrder.ActualOrderService;
import pl.nawrockiit.catering3.newMenu.NewMenu;
import pl.nawrockiit.catering3.newMenu.NewMenuService;
import pl.nawrockiit.catering3.newOrder.NewOrder;
import pl.nawrockiit.catering3.newOrder.NewOrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderSummaryServiceImpl implements OrderSummaryService {

    private final OrderSummaryRepository orderSummaryRepository;
    private final NewOrderService newOrderService;
    private final ActualOrderService actualOrderService;
    private final NewMenuService newMenuService;

    @Override
    public void deleteAll() {
        orderSummaryRepository.deleteAll();
    }


    @Override
    public void copyNewOrdersToActualOrders() {
        List<NewOrder> newOrdersList = newOrderService.findAll();
        ModelMapper modelMapper = new ModelMapper();

        for (NewOrder newOrder : newOrdersList) {
            ActualOrder actualOrder;
            actualOrder = modelMapper.map(newOrder, ActualOrder.class);
            actualOrderService.save(actualOrder);
        }
    }

    @Override
    public void collectOrderSummary() {
        List<NewMenu> newMenuList = newMenuService.findAll();
        List<ActualOrder> actualOrderMeals = actualOrderService.findAll();

        for (NewMenu newMenu : newMenuList) {
            if (!newMenu.getMealName().equals("Brak")) {
                String idsFirstShift = "";
                Integer idsFirstShiftQty = 0;
                String idsSecondShift = "";
                Integer idsSecondShiftQty = 0;
                for (ActualOrder actualOrderMeal : actualOrderMeals) {
                    if (newMenu.getMealNo().equals(actualOrderMeal.getMealMon())) {
                        if (actualOrderMeal.getShiftMon() == 1) {
                            if (actualOrderMeal.getMealMonName().substring(0, 2).equals("2x")){
                                idsFirstShiftQty++;
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsFirstShiftQty++;
                            }
                        } else if (actualOrderMeal.getShiftMon() == 2) {
                            if (actualOrderMeal.getMealMonName().substring(0, 2).equals("2x")){
                                idsSecondShiftQty++;
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        }
                    }
                    if (newMenu.getMealNo().equals(actualOrderMeal.getMealTue())) {
                        if (actualOrderMeal.getShiftTue() == 1) {
                            if (actualOrderMeal.getMealTueName().substring(0, 2).equals("2x")){
                                idsFirstShiftQty++;
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        } else if (actualOrderMeal.getShiftTue() == 2) {
                            if (actualOrderMeal.getMealTueName().substring(0, 2).equals("2x")){
                                idsSecondShiftQty++;
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        }
                    }
                    if (newMenu.getMealNo().equals(actualOrderMeal.getMealWed())) {
                        if (actualOrderMeal.getShiftWed() == 1) {
                            if (actualOrderMeal.getMealWedName().substring(0, 2).equals("2x")){
                                idsFirstShiftQty++;
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        } else if (actualOrderMeal.getShiftWed() == 2) {
                            if (actualOrderMeal.getMealWedName().substring(0, 2).equals("2x")){
                                idsSecondShiftQty++;
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        }
                    }
                    if (newMenu.getMealNo().equals(actualOrderMeal.getMealThu())) {
                        if (actualOrderMeal.getShiftThu() == 1) {
                            if (actualOrderMeal.getMealThuName().substring(0, 2).equals("2x")){
                                idsFirstShiftQty++;
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        } else if (actualOrderMeal.getShiftThu() == 2) {
                            if (actualOrderMeal.getMealThuName().substring(0, 2).equals("2x")){
                                idsSecondShiftQty++;
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        }
                    }
                    if (newMenu.getMealNo().equals(actualOrderMeal.getMealFri())) {
                        if (actualOrderMeal.getShiftFri() == 1) {
                            if (actualOrderMeal.getMealFriName().substring(0, 2).equals("2x")){
                                idsFirstShiftQty++;
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsFirstShiftQty++;
                                idsFirstShift = idsFirstShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        } else if (actualOrderMeal.getShiftFri() == 2) {
                            if (actualOrderMeal.getMealFriName().substring(0, 2).equals("2x")){
                                idsSecondShiftQty++;
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            } else {
                                idsSecondShiftQty++;
                                idsSecondShift = idsSecondShift + actualOrderMeal.getUser().getUserId() + ", ";
                            }
                        }
                    }

                }
                StringBuilder idsFirstShiftSorted;
                if (!idsFirstShift.equals("")) {
                    String[] idsFirstShiftArray = idsFirstShift.split(", ");
                    List<Integer> idsFirstShiftInteger = new ArrayList<>();
                    for (String id : idsFirstShiftArray) {
                        idsFirstShiftInteger.add(Integer.parseInt(id));
                    }
                    Collections.sort(idsFirstShiftInteger);
                    idsFirstShiftSorted = new StringBuilder();
                    for (Integer id : idsFirstShiftInteger) {
                        idsFirstShiftSorted.append(id).append(", ");
                    }
                } else {
                    idsFirstShiftSorted = new StringBuilder("");
                }

                StringBuilder idsSecondShiftSorted;
                if (!idsSecondShift.equals("")) {
                    String[] idsSecondShiftArray = idsSecondShift.split(", ");
                    List<Integer> idsSecondShiftInteger = new ArrayList<>();
                    for (String id : idsSecondShiftArray) {
                        idsSecondShiftInteger.add(Integer.parseInt(id));
                    }
                    Collections.sort(idsSecondShiftInteger);
                    idsSecondShiftSorted = new StringBuilder();
                    for (Integer id : idsSecondShiftInteger) {
                        idsSecondShiftSorted.append(id).append(", ");
                    }
                } else {
                    idsSecondShiftSorted = new StringBuilder("");
                }
//                String[] idsFirstShiftArray = idsFirstShift.split(", ");
//                Arrays.sort(idsFirstShiftArray);
//                StringBuilder idsFirstShiftSorted = new StringBuilder();
//                for (String id : idsFirstShiftArray) {
//                    idsFirstShiftSorted.append(id).append(", ");
//                }
//
//                String[] idsSecondShiftArray = idsSecondShift.split(", ");
//                Arrays.sort(idsSecondShiftArray);
//                StringBuilder idsSecondShiftSorted = new StringBuilder();
//                for (String id : idsSecondShiftArray) {
//                    idsSecondShiftSorted.append(id).append(", ");
//                }

                if (idsFirstShiftQty != 0 || idsSecondShiftQty != 0) {
                    OrderSummary orderSummary = new OrderSummary();
                    orderSummary.setMealPrice(newMenu.getMealPrice());
                    orderSummary.setPeriod(newMenu.getPeriod());
                    orderSummary.setPeriodDate(newMenu.getPeriodDate());
                    orderSummary.setMealNo(newMenu.getMealNo());
                    orderSummary.setMealName(newMenu.getMealName());
                    orderSummary.setDayId(newMenu.getDayId());
                    orderSummary.setPeriod(newMenu.getPeriod());
                    orderSummary.setPeriodDate(newMenu.getPeriodDate());
                    orderSummary.setFirstShiftQuantity(idsFirstShiftQty);
                    orderSummary.setFirstShiftUsersId(idsFirstShiftSorted.toString());
                    orderSummary.setSecondShiftQuantity(idsSecondShiftQty);
                    orderSummary.setSecondShiftUsersId(idsSecondShiftSorted.toString());
                    save(orderSummary);
                }
            }
        }
    }

    @Override
    public OrderSummary save(OrderSummary orderSummary) {
        return orderSummaryRepository.save(orderSummary);
    }

    @Override
    public List<OrderSummary> findAll() {
        return orderSummaryRepository.findAll();
    }

    @Override
    public List<OrderSummary> findOrderSummaryByDayId(Integer dayId) {
        return orderSummaryRepository.findOrderSummaryByDayId(dayId);
    }

}
