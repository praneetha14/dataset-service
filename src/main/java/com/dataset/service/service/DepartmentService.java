package com.dataset.service.service;

import com.dataset.service.model.DepartmentDTO;
import com.dataset.service.model.RecordCreateResponseVO;

public interface DepartmentService {
    RecordCreateResponseVO createDepartment(DepartmentDTO departmentDTO);
}
