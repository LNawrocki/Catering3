package pl.nawrockiit.catering3.newMenu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

@Service
@AllArgsConstructor
public class NewMenuServiceImpl implements NewMenuService{

    public final NewMenuRepository newMenuRepository;

    @Override
    public List<NewMenu> newMenuFindByDayId(Integer id) {
        return newMenuRepository.findByDayId(id);
    }

    @Override
    public NewMenu save(NewMenu newMenu) {
        return newMenuRepository.save(newMenu);
    }

    @Override
    public void deleteMeal(Integer mealNo) {
        newMenuRepository.deleteNewMenuByMealNo(mealNo);
    }

    @Override
    public void deleteByDayNo(Integer dayId) {
        newMenuRepository.deleteByDayNo(dayId);
    }

    @Override
    public NewMenu findByMealNo(Integer mealNo) {
        return newMenuRepository.findByMealNo(mealNo);
    }

    @Override
    public Boolean newMenuListNotEmpty() {
        return !newMenuRepository.findAll().isEmpty();
    }

    @Override
    public List<NewMenu> findNewMenusByDayId(Integer id) {
        return newMenuRepository.findNewMenusByDayId(id);
    }

    @Override
    public void deleteAll() {
        newMenuRepository.deleteAll();
    }

    @Override
    public void setBrakFirstMealDay( String period, String periodDate) {
//        setDefaultMealDay(period, periodDate);
        for (int i = 1; i <= 5; i++) {
            NewMenu newMenu = new NewMenu();
            newMenu.setMealNo(i);
            newMenu.setMealName("Brak");
            newMenu.setDayId(i);
            newMenu.setPeriod(period);
            newMenu.setPeriodDate(periodDate);
            newMenu.setMealPrice(BigDecimal.valueOf(0.00));
            save(newMenu);
        }
    }

    @Override
    public List<NewMenu> findAll() {
        return newMenuRepository.findAll();
    }

    @Override
    public Integer lastIndex() {
        return newMenuRepository.lastIndex();
    }


//    private void setDefaultMealDay( String period, String periodDate) {
//        for (int i = 1; i <= 5; i++) {
//            NewMenu newMenu = new NewMenu();
//            newMenu.setMealNo(i);
//            newMenu.setMealName("Brak");
//            newMenu.setDayId(i);
//            newMenu.setPeriod(period);
//            newMenu.setPeriodDate(periodDate);
//            newMenu.setMealPrice(BigDecimal.valueOf(0.00));
//            save(newMenu);
//        }
//    }

}
