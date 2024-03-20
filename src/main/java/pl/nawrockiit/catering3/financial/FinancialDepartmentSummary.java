package pl.nawrockiit.catering3.financial;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@ToString
public class FinancialDepartmentSummary {

    private String departmentName ="";
    private BigDecimal departmentSummaryFullPrice = new BigDecimal(0);
    private BigDecimal departmentSummaryDiscountPrice = new BigDecimal(0);
    private Integer notPaidOrders = 0;
}
