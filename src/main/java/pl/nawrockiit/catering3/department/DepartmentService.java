package pl.nawrockiit.catering3.department;

import java.util.List;


public interface DepartmentService {

    List<Department> findAll();
    Department save(Department department);

    void delete(Department department);

    Department getById(Integer departmentId);
    Boolean isUserByDepartmentId(Integer id);

    Department getDepartmentById(Integer id);
}
