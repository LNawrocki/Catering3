package pl.nawrockiit.catering3.newMenu;

import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.nawrockiit.catering3.dish.DishService;
import pl.nawrockiit.catering3.newOrder.NewOrderService;
import pl.nawrockiit.catering3.price.PriceService;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Controller
public class NewMenuController {

    private final Map<String, String> companyData;
    private final NewMenuService newMenuService;
    private final PriceService priceService;
    private final DishService dishService;
    private final NewOrderService newOrderService;

    public NewMenuController(@Value("#{${companyData}}") Map<String, String> companyData, NewMenuService newMenuService, PriceService priceService, DishService dishService, NewOrderService newOrderService) {
        this.companyData = companyData;
        this.newMenuService = newMenuService;
        this.priceService = priceService;
        this.dishService = dishService;
        this.newOrderService = newOrderService;
    }

    @PostMapping("/admin/newMenu/init")
    public String newMenuInit(@RequestParam String period,
                              @RequestParam String periodDate) {
        newMenuService.setBrakFirstMealDay(period, periodDate);
        return "redirect:/admin/newMenu/edit";
    }

    @GetMapping("/admin/newMenu/edit")
    public String newMenuEditView(Model model) {
        model.addAttribute("newMenu", new NewMenu());
        model.addAttribute("companyData", companyData);

        if (newMenuService.findAll().isEmpty()) {
            model.addAttribute("editDate", true);
        } else {
            model.addAttribute("editDate", false);
        }

        if (newMenuService.lastIndex() == null) {
            model.addAttribute("lastIndex", 1);
        } else {
            model.addAttribute("lastIndex", newMenuService.lastIndex() + 1);
        }

        model.addAttribute("mealsMonday", Optional.ofNullable(newMenuService.newMenuFindByDayId(1)).orElse(new ArrayList<>()));
        model.addAttribute("mealsTuesday", Optional.ofNullable(newMenuService.newMenuFindByDayId(2)).orElse(new ArrayList<>()));
        model.addAttribute("mealsWednesday", Optional.ofNullable(newMenuService.newMenuFindByDayId(3)).orElse(new ArrayList<>()));
        model.addAttribute("mealsThursday", Optional.ofNullable(newMenuService.newMenuFindByDayId(4)).orElse(new ArrayList<>()));
        model.addAttribute("mealsFriday", Optional.ofNullable(newMenuService.newMenuFindByDayId(5)).orElse(new ArrayList<>()));
        model.addAttribute("prices", Optional.ofNullable(priceService.findAll()).orElse(new ArrayList<>()));
        model.addAttribute("dishes", Optional.ofNullable(dishService.findAll()).orElse(new ArrayList<>()));
        model.addAttribute("pageId", 8);

        if (newMenuService.lastIndex() == null) {
            model.addAttribute("period", "na KW: " + (LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()) + 1));
            model.addAttribute("periodDate", LocalDate.now().plusWeeks(1).with(DayOfWeek.MONDAY) + " - " + LocalDate.now().plusWeeks(1).with(DayOfWeek.FRIDAY));
        } else {
            model.addAttribute("period", newMenuService.findAll().get(0).getPeriod());
            model.addAttribute("periodDate", newMenuService.findAll().get(0).getPeriodDate());
        }

        if (newOrderService.getQuantityOfNewOrders() == 0) {
            model.addAttribute("deleteButtonVisible", true);
        } else {
            model.addAttribute("deleteButtonVisible", false);
        }
        return "/admin/new-menu-edit";
    }


    @PostMapping("/admin/newMenu/addMeal")
    public String newMenuAdd(@RequestParam String period,
                             @RequestParam String periodDate,
                             NewMenu newMenu) {
        if (newMenuService.findAll().size() < 5) {
            newMenuService.deleteAll();
            newMenuService.setBrakFirstMealDay(period, periodDate);
            if (!newMenu.getMealName().equals("") && newMenu.getMealName() != null) {
                newMenu.setMealNo(6);
                newMenu.setMealName(newMenu.getMealName().trim());
                newMenuService.save(newMenu);
            }
            return "redirect:/admin/newMenu/edit";

        } else if (!newMenu.getMealName().equals("") && newMenu.getMealName() != null) {
            newMenu.setMealName(newMenu.getMealName().replaceAll("\\s{2,}", " ").trim());
            newMenuService.save(newMenu);
            return "redirect:/admin/newMenu/edit";
        } else {
            return "redirect:/admin/newMenu/edit";
        }
    }

    @PostMapping("/admin/newMenu/deleteMeal")
    public String deleteMenu(@RequestParam Integer mealNo) {
        newMenuService.deleteMeal(mealNo);
        return "redirect:/admin/newMenu/edit";
    }

    @PostMapping("/admin/newMenu/deleteDayMeals")
    public String deleteMenuDay(@RequestParam Integer dayId) {
        newMenuService.deleteByDayNo(dayId);
        return "redirect:/admin/newMenu/edit";
    }

    @PostMapping("/admin/newMenu/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        newMenuService.deleteAll();
        if (file.isEmpty()) {
            return "redirect/newMenu/upload";
        } else {
            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(1);
                Iterator<Row> rows = sheet.iterator();

                Integer rowCounter = 0;
                Integer cellCounter = 0;
                NewMenu newMenuPos = new NewMenu();

                Cell cell = null;
                while (rows.hasNext()) {
                    rowCounter++;
                    Row row = rows.next();
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        cell = cells.next();
                        ;
                        if (rowCounter >= 3) {
                            cellCounter++;
//                            System.out.println(cellCounter);
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    Integer cellContentN = (int) cell.getNumericCellValue();
//                                    System.out.println("komórkaN: " + cellContentN);
                                    switch (cellCounter) {
                                        case 1:
                                            newMenuPos.setMealNo(cellContentN);
                                            break;
                                        case 4:
                                            newMenuPos.setDayId(cellContentN);
                                            break;
                                    }
                                    break;
                                case STRING:
                                    String cellContentS = cell.getStringCellValue();
//                                    System.out.println("komórkaS: " + cellContentS);

                                    switch (cellCounter) {
                                        case 2:
                                            newMenuPos.setMealName(cellContentS);
                                            break;
                                        case 3:
                                            newMenuPos.setMealPrice(BigDecimal.valueOf(Double.valueOf(cellContentS)));
                                            break;
                                        case 5:
                                            newMenuPos.setPeriod(cellContentS);
                                            break;
                                        case 6:
                                            newMenuPos.setPeriodDate(cellContentS);
                                            break;
                                    }
                            }
                        }
                    }
                    cellCounter = 0;
                    if (newMenuPos.getDayId() != null) {
                        newMenuService.save(newMenuPos);
                        System.out.println(newMenuPos);
                    }
                }
                // Tutaj możesz przekazać dane gdziekolwiek chcesz w aplikacji
                // Możesz przetworzyć dane, zapisać je do bazy danych itp.
//                return ResponseEntity.ok("File uploaded successfully");
                return "redirect:/admin/newMenu/edit";
            } catch (IOException e) {
                e.printStackTrace();
//                return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
                return "redirect:/admin/newMenu/edit";

            }
        }
    }

}

