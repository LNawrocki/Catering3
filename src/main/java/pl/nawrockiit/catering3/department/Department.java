package pl.nawrockiit.catering3.department;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data
public class Department {
    @Id
    private Integer id;
    private String name;
    @Range(min = 0, max = 100, message = "Błąd: Wartość musi być w przedziale 0 - 100")
    @Column(name = "payment_perc")
    private Integer paymentPerc;

}
