package com.dataset.service;

import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.deserializer.CustomRecordDTODeserializer;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.repository.EmployeeRepository;
import com.dataset.service.service.factory.DataSetFactory;
import com.dataset.service.service.impl.DepartmentDatasetService;
import com.dataset.service.service.impl.EmployeeDatasetService;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasetServiceAutoConfiguration {

    @Bean
    public DepartmentDatasetService getDepartmentDatasetService(DepartmentRepository departmentRepository,
                                                                EntityManager entityManager) {
        return new DepartmentDatasetService(departmentRepository, entityManager);
    }

    @Bean
    public EmployeeDatasetService getEmployeeDatasetService(EmployeeRepository employeeRepository,
                                                            DepartmentRepository departmentRepository,
                                                            EntityManager entityManager) {
        return new EmployeeDatasetService(employeeRepository, departmentRepository, entityManager);
    }


    @Bean
    public DataSetFactory dataSetFactory(EmployeeDatasetService employeeDatasetService,
                                         DepartmentDatasetService departmentDatasetService) {
        return new DataSetFactory(employeeDatasetService, departmentDatasetService);
    }

    @Bean
    public SimpleModule recordDTOModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RecordDTO.class, new CustomRecordDTODeserializer());
        return module;
    }

    @Bean
    public DataSetServiceProxy dataSetServiceProxy(DataSetFactory dataSetFactory) {
        return new DataSetServiceProxy(dataSetFactory);
    }
}
