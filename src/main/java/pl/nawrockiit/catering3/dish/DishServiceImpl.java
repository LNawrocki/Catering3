package pl.nawrockiit.catering3.dish;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DishServiceImpl implements DishService{

    private final DishRepository dishRepository;
    @Override
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }
    @Override
    public void deleteByDishId(Integer dishId) {
        dishRepository.deleteById(dishId);
    }

    @Override
    public Dish save(Dish dish) {
        return dishRepository.save(dish);
    }

}
