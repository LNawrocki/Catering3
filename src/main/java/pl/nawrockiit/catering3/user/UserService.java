package pl.nawrockiit.catering3.user;



import pl.nawrockiit.catering3.department.Department;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    User getUserById(Integer id);
    User getUserByUsername(String username);
    List<User> findUsersByDepartmentId(Integer id);
    List<User> findUsersByDepartment(Department department);

    List<User> findAll();
    void delete(User user);
    User save(User user);
    Integer firsEmptyIndex();
    Integer lastEmptyIndex();

    void updateUser(String username, String firstName, String lastName, Department department, String password, String role, Boolean active, String email, BigDecimal credit, Integer userId);
    void updateUserMoney(BigDecimal credit, Integer userId);

    List<User> findUsersByLastName();
}
