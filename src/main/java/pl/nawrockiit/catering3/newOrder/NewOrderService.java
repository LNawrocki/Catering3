package pl.nawrockiit.catering3.newOrder;

import java.util.List;

public interface NewOrderService {

    NewOrder getNewOrderByUserId(Integer userId);
    void delete(NewOrder newOrder);
    void deleteAll();
    Integer getQuantityOfNewOrders();
    NewOrder getNewOrderById(Long id);
    List<NewOrder> findAll();
    NewOrder save(NewOrder newOrder);
    List<NewOrder> findNewOrderByIsPaid(Boolean isPaid);


}
