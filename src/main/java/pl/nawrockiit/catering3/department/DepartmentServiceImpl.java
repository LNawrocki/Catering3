package pl.nawrockiit.catering3.department;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.user.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
//
    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void delete(Department department) {
        departmentRepository.delete(department);
    }

    @Override
    public Department getById(Integer departmentId) {
        return departmentRepository.getDepartmentById(departmentId);
    }

    @Override
    public Boolean isUserByDepartmentId(Integer id){
        return userRepository.getByDepartmentId(id).isEmpty();
    }

    @Override
    public Department getDepartmentById(Integer id) {
        return departmentRepository.getDepartmentById(id);
    }
}
