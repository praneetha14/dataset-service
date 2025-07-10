package com.dataset.service.service.impl;

import com.dataset.service.entity.DepartmentEntity;
import com.dataset.service.entity.EmployeeEntity;
import com.dataset.service.model.EmployeeDTO;
import com.dataset.service.model.RecordCreateResponseVO;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.repository.EmployeeRepository;
import com.dataset.service.service.EmployeeService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    @Override
    public RecordCreateResponseVO createEmployee(EmployeeDTO employeeDTO) {
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findById(employeeDTO.getDepartmentId());
        if(optionalDepartmentEntity.isEmpty()){
            throw new RuntimeException("Department not found with id: " + employeeDTO.getDepartmentId());
        }
        EmployeeEntity employeeEntity = toEntity(employeeDTO, optionalDepartmentEntity.get());
        employeeEntity = employeeRepository.save(employeeEntity);
        return new RecordCreateResponseVO("Record created successfully", "Employee", employeeEntity.getId());
    }

    private EmployeeEntity toEntity(EmployeeDTO employeeDTO, DepartmentEntity departmentEntity) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setAge(employeeDTO.getAge());
        employeeEntity.setDepartment(departmentEntity);
        return employeeEntity;
    }
}
