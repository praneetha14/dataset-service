package com.dataset.service.service.factory;

import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.impl.DepartmentDatasetService;
import com.dataset.service.service.impl.EmployeeDatasetService;

public record DataSetFactory(EmployeeDatasetService employeeDatasetService,
                             DepartmentDatasetService departmentDatasetService) {
    public DataSetService getService(String dataSetName) {
        return switch (SupportedDataSets.fromValue(dataSetName)) {
            case DEPARTMENT -> departmentDatasetService;
            case EMPLOYEE -> employeeDatasetService;
        };
    }
}
