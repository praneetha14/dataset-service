package com.dataset.service;

import com.dataset.service.model.ApplicationProperties;
import com.dataset.service.model.deserializer.CustomRecordDTODeserializer;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.repository.EmployeeRepository;
import com.dataset.service.service.factory.DataSetFactory;
import com.dataset.service.service.impl.DepartmentDatasetService;
import com.dataset.service.service.impl.EmployeeDatasetService;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class DatasetServiceAutoConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

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

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.servers(List.of(new Server().url(applicationProperties.getBaseUrl())));
        return openAPI;
    }
}
