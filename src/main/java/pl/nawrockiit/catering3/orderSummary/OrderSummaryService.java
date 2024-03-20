package pl.nawrockiit.catering3.orderSummary;

import java.util.List;

public interface OrderSummaryService {
    void deleteAll();

    void copyNewOrdersToActualOrders();

    void collectOrderSummary();

    OrderSummary save(OrderSummary orderSummary);

    List<OrderSummary> findAll();

    List<OrderSummary> findOrderSummaryByDayId(Integer dayId);

}
