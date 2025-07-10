package com.dataset.service.service;

import com.dataset.service.model.EmployeeDTO;
import com.dataset.service.model.RecordCreateResponseVO;

public interface EmployeeService {
    RecordCreateResponseVO createEmployee(EmployeeDTO employeeDTO);
}
