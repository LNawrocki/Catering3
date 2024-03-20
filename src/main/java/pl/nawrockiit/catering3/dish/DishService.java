package pl.nawrockiit.catering3.dish;

import java.util.List;

public interface DishService {
    List<Dish> findAll();
    void deleteByDishId(Integer dishId);

    Dish save(Dish dish);
}
