package pl.nawrockiit.catering3.price;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class PriceController {

    private final Map<String, String> companyData;
    private final PriceService priceService;

    public PriceController(@Value("#{${companyData}}") Map<String, String> companyData, PriceService priceService) {
        this.companyData = companyData;
        this.priceService = priceService;
    }

    @GetMapping("/price")
    public String pricesView(Model model) {
        model.addAttribute("price", new Price());
        model.addAttribute("prices", priceService.findAll());
        model.addAttribute("pageId", 14);
        model.addAttribute("companyData", companyData);
        return "/admin/price-list";
    }

    @PostMapping("/price")
    public String priceAdd(Price price){
        priceService.save(price);
        return "redirect:/admin/price";
    }

    @PostMapping("/price/delete")
    public String priceDelete(@RequestParam Integer deletePriceId){
        priceService.deleteByPriceId(deletePriceId);
        return "redirect:/admin/price";
    }
}
