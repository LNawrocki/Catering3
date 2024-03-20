package pl.nawrockiit.catering3.actualOrder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActualOrderRepository extends JpaRepository<ActualOrder, Long> {

    ActualOrder getActualOrderByUser_UserId(Integer userId);

}
