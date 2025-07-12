package com.dataset.service.proxy;

import com.dataset.service.AbstractTest;
import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.model.request.DepartmentDTO;
import com.dataset.service.model.request.EmployeeDTO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.DepartmentVO;
import com.dataset.service.model.response.EmployeeVO;
import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.service.impl.DepartmentDatasetService;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DatasetProxyIntegrationTest extends AbstractTest {

    private static final String RECORD_CREATION_SUCCESS_MESSAGE = "Record created successfully";
    private static final String RECORD_CREATION_WITH_INVALID_DEPARTMENT = "Department not found with id: ";
    private static final String UNSUPPORTED_DATASET_TYPE = "DataSet is not supported";
    private static final String SORTED_RECORDS_MESSAGE = "Fetched sorted records successfully";
    private static final String GROUPED_RECORDS_MESSAGE = "Fetched grouped records successfully";
    private static final String SORTED_AND_GROUPED_RECORDS_MESSAGE = "Fetched sorted and grouped records successfully";
    private static final String FETCHED_ALL_RECORDS_MESSAGE = "Fetched all records successfully";
    private static final String GROUPED_RECORDS_ERROR_MESSAGE = "Error getting grouped records";

    @Autowired
    private DataSetServiceProxy dataSetServiceProxy;

    @Autowired
    private DepartmentDatasetService departmentDatasetService;

    @Test
    void createRecordForEmployeeSuccessTest() {
        RecordDTO departmentDTO = createDepartmentDTO();
        RecordCreateResponseVO createResponseVO = departmentDatasetService.createRecord(departmentDTO);
        RecordDTO employeeDTO = createEmployeeDTO(createResponseVO);
        RecordCreateResponseVO createResponseVO2 = dataSetServiceProxy.createRecord(employeeDTO,
                SupportedDataSets.EMPLOYEE.getValue());
        assertNotNull(createResponseVO2);
        assertEquals(RECORD_CREATION_SUCCESS_MESSAGE, createResponseVO2.message());
        assertEquals(SupportedDataSets.EMPLOYEE.getValue(), createResponseVO2.dataset());
    }

    @Test
    void createRecordForEmployeeWithInvalidDepartmentFailureTest() {
        UUID invalidDepartmentId = UUID.randomUUID();
        RecordDTO employeeDTO = createEmployeeDTO(new RecordCreateResponseVO(null, null,
                invalidDepartmentId));
        Throwable exception = assertThrows(RuntimeException.class, () -> dataSetServiceProxy.createRecord(employeeDTO,
                SupportedDataSets.EMPLOYEE.getValue()));
        assertNotNull(exception);
        assertEquals(RECORD_CREATION_WITH_INVALID_DEPARTMENT + invalidDepartmentId, exception.getMessage());
    }

    @Test
    void createRecordForDepartmentSuccessTest() {
        RecordDTO departmentDTO = createDepartmentDTO();
        RecordCreateResponseVO createResponseVO = dataSetServiceProxy.createRecord(departmentDTO,
                SupportedDataSets.DEPARTMENT.getValue());
        assertNotNull(createResponseVO);
        assertEquals(RECORD_CREATION_SUCCESS_MESSAGE, createResponseVO.message());
        assertEquals(SupportedDataSets.DEPARTMENT.getValue(), createResponseVO.dataset());
    }

    @Test
    void createRecordWithInvalidDataSetNameFailureTest() {
        RecordDTO departmentDTO = createDepartmentDTO();
        Throwable exception = assertThrows(RuntimeException.class, () -> dataSetServiceProxy.createRecord(departmentDTO,
                "invalid"));
        assertNotNull(exception);
        assertEquals(UNSUPPORTED_DATASET_TYPE, exception.getMessage());
    }

    @Test
    void searchRecordSortByInEmployeeSuccessTest() {
        createTestEmployees();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), "departmentName", null,
                        "DESC");
        List<EmployeeVO> employeeVOS = (List<EmployeeVO>) searchResponse.records();

        assertNotNull(searchResponse);
        assertEquals(SORTED_RECORDS_MESSAGE, searchResponse.message());
    }

    @Test
    void searchRecordGroupByInEmployeeSuccessTest() {
        createTestEmployees();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), null, "departmentName", null);
        assertNotNull(searchResponse);
        assertEquals(GROUPED_RECORDS_MESSAGE, searchResponse.message());
    }

    @Test
    void searchRecordSortByAndGroupByInEmployeeSuccessTest() {
        createTestEmployees();
        RecordResponseVO recordResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), "age", "departmentName", "DESC");
        assertNotNull(recordResponse);
        assertEquals(SORTED_AND_GROUPED_RECORDS_MESSAGE, recordResponse.message());
    }

    @Test
    void searchRecordsInEmployeeSuccessTest() {
        createTestEmployees();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), null,
                        null, null);
        assertNotNull(searchResponse);
        assertEquals(FETCHED_ALL_RECORDS_MESSAGE, searchResponse.message());
    }

    @Test
    void searchRecordsSortByInDepartmentSuccessTest() {
        createTestDepartments();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.DEPARTMENT.getValue(), "name", null, "DESC");
        List<DepartmentVO> departmentVOS = (List<DepartmentVO>) searchResponse.sortedRecords();
        assertNotNull(searchResponse);
        assertEquals(SORTED_RECORDS_MESSAGE, searchResponse.message());
        assertEquals(2, departmentVOS.size());
    }

    @Test
    void searchRecordsGroupByInDepartmentSuccessTest() {
        createTestDepartments();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.DEPARTMENT.getValue(), null, "location",
                        null);
        assertNotNull(searchResponse);
        assertEquals(GROUPED_RECORDS_MESSAGE, searchResponse.message());
    }

    @Test
    void searchRecordsSortByAndGroupByInDepartmentSuccessTest() {
        createTestDepartments();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.DEPARTMENT.getValue(), "name", "location", "DESC");
        assertNotNull(searchResponse);
        assertEquals(SORTED_AND_GROUPED_RECORDS_MESSAGE, searchResponse.message());
    }

    @Test
    void searchRecordsInDepartmentSuccessTest() {
        createTestDepartments();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.DEPARTMENT.getValue(), null, null, null);
        List<DepartmentVO> departmentVOS = (List<DepartmentVO>) searchResponse.records();
        assertNotNull(searchResponse);
        assertEquals(FETCHED_ALL_RECORDS_MESSAGE, searchResponse.message());
        assertEquals(2, departmentVOS.size());
    }

    @Test
    void searchSortByAndGroupByFailureTest() {
        Throwable exception = assertThrows(RuntimeException.class, () -> dataSetServiceProxy
                .getSortedAndGroupedRecords("invalid", null, null, null));
        assertNotNull(exception);
        assertEquals(UNSUPPORTED_DATASET_TYPE, exception.getMessage());
    }

    @Test
    void searchGroupByInDepartmentWithInvalidFieldFailureTest() {
        createTestDepartments();
        Throwable exception = assertThrows(RuntimeException.class,
                () -> dataSetServiceProxy.getSortedAndGroupedRecords(SupportedDataSets.DEPARTMENT.getValue(), null,
                        "test", null));
        assertNotNull(exception);
        assertEquals(GROUPED_RECORDS_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void searchGroupByInEmployeeWithInvalidFieldFailureTest() {
        createTestEmployees();
        Throwable exception = assertThrows(RuntimeException.class,
                () -> dataSetServiceProxy.getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), null,
                        "test", null));
        assertNotNull(exception);
        assertEquals(GROUPED_RECORDS_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void searchSortedByIdInEmployeeSuccessTest() {
        createTestEmployees();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), "id", null, "ASC");
        List<EmployeeVO> employeeVOS = (List<EmployeeVO>) searchResponse.sortedRecords();
        assertNotNull(searchResponse);
        assertEquals(SORTED_RECORDS_MESSAGE, searchResponse.message());
        assertEquals(2, employeeVOS.size());
    }

    @Test
    void searchSortByLocationInEmployeeSuccessTest() {
        createTestEmployees();
        RecordResponseVO searchResponse = dataSetServiceProxy
                .getSortedAndGroupedRecords(SupportedDataSets.EMPLOYEE.getValue(), "location", null, "ASC");
        assertNotNull(searchResponse);
        assertEquals(SORTED_RECORDS_MESSAGE, searchResponse.message());
    }

    private void createTestDepartments() {
        RecordDTO departmentDTO = createDepartmentDTO();
        departmentDatasetService.createRecord(departmentDTO);
        RecordDTO departmentDTO2 = createDepartmentDTO();
        departmentDatasetService.createRecord(departmentDTO2);
    }

    private void createTestEmployees() {
        RecordDTO departmentDTO = createDepartmentDTO();
        RecordCreateResponseVO createResponseVO = departmentDatasetService.createRecord(departmentDTO);
        RecordDTO employeeDTO1 = createEmployeeDTO(createResponseVO);
        dataSetServiceProxy.createRecord(employeeDTO1, SupportedDataSets.EMPLOYEE.getValue());
        RecordDTO employeeDTO2 = createEmployeeDTO(createResponseVO);
        dataSetServiceProxy.createRecord(employeeDTO2, SupportedDataSets.EMPLOYEE.getValue());
    }

    private DepartmentDTO createDepartmentDTO() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentName("Department 1");
        departmentDTO.setLocation("INDIA");
        return departmentDTO;
    }

    private EmployeeDTO createEmployeeDTO(RecordCreateResponseVO createResponseVO) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setAge(25);
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartmentId(createResponseVO.id());
        return employeeDTO;
    }
}
