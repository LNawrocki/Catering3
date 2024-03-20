package pl.nawrockiit.catering3.info;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class InfoController {

    private final Map<String, String> companyData;
    private final InfoService infoService;

    public InfoController(@Value("#{${companyData}}") Map<String, String> companyData, InfoService infoService) {
        this.companyData = companyData;
        this.infoService = infoService;
    }

    @GetMapping("/editInfo")
    public String addDepartmentView(Model model) {
        model.addAttribute("info", new Info());
        model.addAttribute("informations", infoService.findAll());
        model.addAttribute("companyData", companyData);
        return "/admin/info-edit";
    }

    @PostMapping("/editInfo")
    public String addDepartment(@Valid Info info, BindingResult bindingResult, Model model) {
        model.addAttribute("companyData", companyData);
        if (bindingResult.hasErrors()) {
            model.addAttribute("informations", infoService.findAll());
            model.addAttribute("pageId", 21);
            return "admin/info-edit";
        }
        infoService.save(info);
        return "redirect:/admin/editInfo";
    }

    @PostMapping("/deleteInfo")
    public String deleteRule(@RequestParam Integer deleteInfoId) {
        Info info = infoService.getInfoByInfoId(deleteInfoId);
        infoService.delete(info);
        return "redirect:/admin/editInfo";
    }
}
