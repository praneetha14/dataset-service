package com.dataset.service.service.factory;

import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.impl.DepartmentDatasetService;
import com.dataset.service.service.impl.EmployeeDatasetService;

public record DataSetFactory(EmployeeDatasetService employeeDatasetService, DepartmentDatasetService departmentDatasetService) {
    public DataSetService getService(String dataSetName) {
        if (SupportedDataSets.EMPLOYEE.getValue().equalsIgnoreCase(dataSetName)) {
            return employeeDatasetService;
        } else if (SupportedDataSets.DEPARTMENT.getValue().equalsIgnoreCase(dataSetName)) {
            return departmentDatasetService;
        } else {
            throw new RuntimeException("Unsupported dataset name: " + dataSetName);
        }
    }
}
