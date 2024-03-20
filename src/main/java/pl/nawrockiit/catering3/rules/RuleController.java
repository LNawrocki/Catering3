package pl.nawrockiit.catering3.rules;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RuleController {
    private final Map<String, String> companyData;
    private final RuleService ruleService;

    public RuleController(@Value("#{${companyData}}") Map<String, String> companyData, RuleService ruleService) {
        this.companyData = companyData;
        this.ruleService = ruleService;
    }

    @GetMapping("/rules")
    public String ruleView(Model model) {
        model.addAttribute("companyData", companyData);
        model.addAttribute("rules", ruleService.findAll());
        model.addAttribute("pageId", 20);
        return "/rules";
    }

    @GetMapping("/admin/editRules")
    public String editRuleView(Model model) {
        model.addAttribute("companyData", companyData);
        model.addAttribute("rule", new Rule());
        model.addAttribute("rules", ruleService.findAll());
        model.addAttribute("pageId", 13);
        return "/admin/rules-edit";
    }

    @PostMapping("/admin/editRules")
    public String editRule(@Valid Rule rule, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rules", ruleService.findAll());
            return "admin/rules-edit";
        }
        ruleService.save(rule);
        return "redirect:/admin/editRules";
    }

    @PostMapping("/admin/deleteRule")
    public String deleteInfo(@RequestParam Integer deleteRuleId) {
        Rule rule = ruleService.getRuleByRuleId(deleteRuleId);
        ruleService.delete(rule);
        return "redirect:/admin/editRules";
    }
}
