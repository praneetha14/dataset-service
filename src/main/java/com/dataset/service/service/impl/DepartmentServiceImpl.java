package com.dataset.service.service.impl;

import com.dataset.service.entity.DepartmentEntity;
import com.dataset.service.model.DepartmentDTO;
import com.dataset.service.model.RecordCreateResponseVO;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.service.DepartmentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public RecordCreateResponseVO createDepartment(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = toEntity(departmentDTO);
        departmentEntity = departmentRepository.save(departmentEntity);
        return new RecordCreateResponseVO("Created Record successfully", "Department", departmentEntity.getId());
    }

    private DepartmentEntity toEntity(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setName(departmentDTO.getDepartmentName());
        departmentEntity.setLocation(departmentDTO.getLocation());
        return departmentEntity;
    }
}
