package pl.nawrockiit.catering3.department;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes({"msg"})
@RequestMapping("/admin")
public class DepartmentController {

    private final Map<String, String> companyData;
    private final DepartmentService departmentService;

    public DepartmentController(@Value("#{${companyData}}") Map<String, String> companyData, DepartmentService departmentService) {
        this.companyData = companyData;
        this.departmentService = departmentService;
    }

    @GetMapping("/department")
    public String addDepartmentView(Model model, HttpSession session) {
        model.addAttribute("department", new Department());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("msg", session.getAttribute("msg"));
        model.addAttribute("pageId", 7);
        model.addAttribute("companyData", companyData);
        return "/admin/department-edit";
    }

    @PostMapping("/department")
    public String addDepartment(@Valid Department department, BindingResult bindingResult, Model model, HttpSession session) {
        model.addAttribute("companyData", companyData);

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "admin/department-edit";
        }
        departmentService.save(department);
        model.addAttribute("msg", "");
        return "redirect:/admin/department";
    }


    @PostMapping("/department/delete")
    public String deleteDepartment(@RequestParam Integer deleteDepartmentId, Model model) {
        model.addAttribute("companyData", companyData);

        if (departmentService.isUserByDepartmentId(deleteDepartmentId)) {
            departmentService.delete(departmentService.getById(deleteDepartmentId));
            model.addAttribute("msg", "");
        } else {
            model.addAttribute("msg", "Odmowa - nie możesz usunąć działu, do którego należą użytkownicy");
        }
        return "redirect:/admin/department";
    }
}
