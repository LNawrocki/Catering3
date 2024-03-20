package pl.nawrockiit.catering3.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.department.Department;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    public final UserRepository userRepository;

    @Override
    public User getUserById(Integer id) {
        return userRepository.getByUserId(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public List<User> findUsersByDepartmentId(Integer id) {
        return userRepository.getByDepartmentId(id);
    }

    @Override
    public List<User> findUsersByDepartment(Department department) {
        return userRepository.findAllByDepartment(department);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Integer firsEmptyIndex() {
        return userRepository.firsEmptyIndex();
    }

    @Override
    public Integer lastEmptyIndex() {
        return userRepository.lastEmptyIndex()+1;
    }

    @Override
    public void updateUser(String username, String firstName, String lastName, Department department, String password, String role, Boolean active, String email, BigDecimal credit, Integer userId) {
        userRepository.updateUser(username, firstName, lastName, department, password, role, active, email, credit, userId);
    }

    @Override
    public void updateUserMoney(BigDecimal credit, Integer userId) {
        userRepository.updateUserMoney(credit , userId);
    }

    @Override
    public List<User> findUsersByLastName() {
        return userRepository.findUsersByLastName();
    }


}
