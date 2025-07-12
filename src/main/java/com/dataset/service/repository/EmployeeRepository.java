package com.dataset.service.repository;

import com.dataset.service.entity.EmployeeEntity;
import com.dataset.service.model.response.EmployeeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    @Query("SELECT new com.dataset.service.model.response.EmployeeVO(e.id, e.name, d.name, d.location, e.age) "
            + "from EmployeeEntity e join DepartmentEntity d on e.department.id = d.id")
    List<EmployeeVO> findAllEmployeeDetails();
}
