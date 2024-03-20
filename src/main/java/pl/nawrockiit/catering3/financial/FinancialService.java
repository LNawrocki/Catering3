package pl.nawrockiit.catering3.financial;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialService {

    List<FinancialDepartmentSummary> getfinancialDepartmentSummaryList();
    BigDecimal getSumOfDepartmentDiscountPrice();
    BigDecimal getSumOfDepartmentFullPrice();

    void closeWeekRewriteOrdersAndNewMenu();
}
