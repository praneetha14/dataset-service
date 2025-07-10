package com.dataset.service.service.impl;

import com.dataset.service.model.DepartmentDTO;
import com.dataset.service.model.EmployeeDTO;
import com.dataset.service.model.RecordCreateResponseVO;
import com.dataset.service.model.RecordDTO;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.DepartmentService;
import com.dataset.service.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataSetServiceImpl implements DataSetService {

    private final EmployeeService employeeService;

    private final DepartmentService departmentService;

    @Override
    public RecordCreateResponseVO createRecord(RecordDTO recordDTO, String dataSetName) {
        if (recordDTO instanceof EmployeeDTO) {
            return employeeService.createEmployee((EmployeeDTO) recordDTO);
        } else if (recordDTO instanceof DepartmentDTO) {
            return departmentService.createDepartment((DepartmentDTO) recordDTO);
        } else {
            throw new RuntimeException("Invalid Payload");
        }
    }
}
