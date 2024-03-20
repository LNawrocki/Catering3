package pl.nawrockiit.catering3.actualOrder;

import java.util.List;

public interface ActualOrderService {

    ActualOrder getActualOrderByUserId(Integer userId);
    void delete(ActualOrder actualOrder);
    List<ActualOrder> findAll();
    ActualOrder save(ActualOrder actualOrder);
    void deleteAll();
}
