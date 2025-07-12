package com.dataset.service.service.factory;

import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.impl.DepartmentDatasetService;
import com.dataset.service.service.impl.EmployeeDatasetService;

/***
 * DataSetFactory is a Factory implementation to resolve the type of DatasetService based om the dataset name provided.
 * @param employeeDatasetService holds the bean of the EmployeeDatasetService
 * @param departmentDatasetService holds the bean of the DepartmentDatasetService
 */


public record DataSetFactory(EmployeeDatasetService employeeDatasetService,
                             DepartmentDatasetService departmentDatasetService) {

    /**
     * The method getService provides the required bean of type DatasetService based on the dataset name provided.
     * @param dataSetName This parameter holds the name of the dataset based on which target bean is resolved.
     * @return DatasetService bean i.e EmployeeDatasetService or DepartmentDatasetService
     */
    public DataSetService getService(String dataSetName) {
        return switch (SupportedDataSets.fromValue(dataSetName)) {
            case DEPARTMENT -> departmentDatasetService;
            case EMPLOYEE -> employeeDatasetService;
        };
    }
}
