package pl.nawrockiit.catering3.department;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Department getDepartmentById(Integer id);
}
