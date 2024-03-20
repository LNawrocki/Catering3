package pl.nawrockiit.catering3.actualOrder;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActualOrderServiceImpl implements ActualOrderService{

    private final ActualOrderRepository actualOrderRepository;

    @Override
    public ActualOrder getActualOrderByUserId(Integer userId) {
        return actualOrderRepository.getActualOrderByUser_UserId(userId);
    }

    @Override
    public void delete(ActualOrder actualOrder) {
        actualOrderRepository.delete(actualOrder);
    }

    @Override
    public List<ActualOrder> findAll() {
        return actualOrderRepository.findAll();
    }

    @Override
    public ActualOrder save(ActualOrder actualOrder) {
        return actualOrderRepository.save(actualOrder);
    }

    @Override
    public void deleteAll() {
        actualOrderRepository.deleteAll();
    }
}
