package pl.nawrockiit.catering3.newMenu;

import java.util.List;

public interface NewMenuService {
    List<NewMenu> newMenuFindByDayId(Integer id);
    NewMenu save(NewMenu newMenu);
    void deleteMeal(Integer mealNo);

    void deleteByDayNo(Integer dayId);
    NewMenu findByMealNo(Integer mealNo);

    Boolean newMenuListNotEmpty();

    List<NewMenu> findNewMenusByDayId(Integer id);
    void deleteAll();

    void setBrakFirstMealDay(String period, String periodDate);

    List<NewMenu> findAll();

    Integer lastIndex();

}
