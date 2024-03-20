package pl.nawrockiit.catering3.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.nawrockiit.catering3.department.Department;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalInt;

public interface UserRepository extends JpaRepository<User, Integer> {

    User getByUserId(Integer userId);
    User getUserByUsername(String username);
//    User getByLogin(String login);
//    User getUserByName(String name);
    List<User> getByDepartmentId(Integer id);
    List<User> findAllByDepartment(Department department);

    @Query(value = "SELECT * FROM users ORDER BY last_name", nativeQuery = true)
    List<User> findUsersByLastName();

    @Query(value ="SELECT MIN(t1.user_id + 1) AS first_missing_id\n " +
            "FROM users AS t1\n LEFT JOIN users AS u2 ON t1.user_id + 1 = u2.user_id\n" +
            "WHERE u2.user_id IS NULL", nativeQuery = true)
    Integer firsEmptyIndex();

    @Query(value ="SELECT max(u.userId) FROM User u")
    Integer lastEmptyIndex();

    @Modifying
    @Transactional
    @Query("update User u set u.username = :username, u.firstName = :firstName, u.lastName = :lastName, u.department =:department,  u.password = :password, u.role = :role, u.active = :active, u.email = :email, u.credit = :credit where u.userId = :userId")
    void updateUser(String username, String firstName, String lastName, Department department, String password, String role, Boolean active, String email, BigDecimal credit, Integer userId);

    @Modifying
    @Transactional
    @Query("update User u set u.credit = :credit where u.userId = :userId")
    void updateUserMoney(BigDecimal credit, Integer userId);
}



