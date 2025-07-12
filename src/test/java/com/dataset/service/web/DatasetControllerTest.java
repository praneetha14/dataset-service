package com.dataset.service.web;

import com.dataset.service.BaseControllerTest;
import com.dataset.service.model.deserializer.CustomRecordDTODeserializer;
import com.dataset.service.model.request.DepartmentDTO;
import com.dataset.service.model.request.EmployeeDTO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.rest.v1.DataSetController;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DataSetController.class)
@ExtendWith(SpringExtension.class)
public class DatasetControllerTest extends BaseControllerTest {

    private static final String DATA_SET_URL = "/api/v1/datasets/";
    private static final String RECORD_CREATE_UEL = "/record";
    private static final String RECORD_QUERY_URL = "/query";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DataSetServiceProxy dataSetServiceProxy;

    @BeforeEach
    void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RecordDTO.class, new CustomRecordDTODeserializer());
        objectMapper.registerModule(module);
    }


    @Test
    void createRecordForEmployeeSuccessTest() throws Exception {
        when(dataSetServiceProxy.createRecord(any(RecordDTO.class), eq("Employee")))
                .thenReturn(new RecordCreateResponseVO("message", "Employee", UUID.randomUUID()));
        mockMvc.perform(
                post(DATA_SET_URL + "Employee" + RECORD_CREATE_UEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeDTO()))
        ).andExpect(status().isCreated());
    }

    @Test
    void createRecordForDepartmentSuccessTest() throws Exception {
        when(dataSetServiceProxy.createRecord(any(RecordDTO.class), eq("Department")))
                .thenReturn(new RecordCreateResponseVO("message", "Department", UUID.randomUUID()));
        mockMvc.perform(
                post(DATA_SET_URL + "Department" + RECORD_CREATE_UEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDepartmentDTO()))
        ).andExpect(status().isCreated());
    }

    @Test
    void createRecordFailureTest() throws Exception {
        mockMvc.perform(
                post(DATA_SET_URL + "Employee" + RECORD_CREATE_UEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("test", "test")))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void searchRecordSuccessTest() throws Exception {
        when(dataSetServiceProxy.getSortedAndGroupedRecords(anyString(), anyString(), any(), any()))
                .thenReturn(new RecordResponseVO("message", "sorted", null, null));
        mockMvc.perform(
                get(DATA_SET_URL + "Employee" + RECORD_QUERY_URL)
                        .param("sortBy", "departmentName")
        ).andExpect(status().isOk());
    }

    @Test
    void searchRecordWithRuntimeExceptionFailureTest() throws Exception {
        when(dataSetServiceProxy.getSortedAndGroupedRecords(anyString(), anyString(), any(), any()))
                .thenThrow(new RuntimeException());
        mockMvc.perform(
                get(DATA_SET_URL + "Employee" + RECORD_QUERY_URL)
                        .param("sortBy", "departmentName")
        ).andExpect(status().isBadRequest());
    }

    private RecordDTO createDepartmentDTO() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentName("Department");
        departmentDTO.setLocation("Location");
        return departmentDTO;
    }

    private RecordDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("test_employee");
        employeeDTO.setAge(25);
        employeeDTO.setDepartmentId(UUID.randomUUID());
        return employeeDTO;
    }
}
