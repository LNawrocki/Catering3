package pl.nawrockiit.catering3.price;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class Price {
    @Id
    @Column(name = "price_id")
    private Integer priceId;
    private BigDecimal price;
    private String description;
}
