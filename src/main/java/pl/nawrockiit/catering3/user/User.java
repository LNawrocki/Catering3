package pl.nawrockiit.catering3.user;

import lombok.Data;
import pl.nawrockiit.catering3.department.Department;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Data
@Table(name ="users")
public class User {
    @Id
    @Column(name = "user_id", unique = true )
    private Integer userId;
    @Column(unique = true)
    @Size(min = 4, message = "Długość nazwy użytkownika musi mieć przynajmniej 4 znaki")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(nullable = false)
    @Size(min = 4, message = "Długość hasła użytkownika musi mieć przynajmniej 4 znaki")
    private String password;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private Boolean active = false;
    private String email;
    @Min(value = 0, message = "Wprowadź wartość większą od zera")
    private BigDecimal credit;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", department=" + department +
                '}';
    }
}
