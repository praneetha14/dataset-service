package com.dataset.service;

import com.dataset.service.model.RecordDTO;
import com.dataset.service.model.deserializer.CustomRecordDTODeserializer;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.repository.EmployeeRepository;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.DepartmentService;
import com.dataset.service.service.EmployeeService;
import com.dataset.service.service.impl.DataSetServiceImpl;
import com.dataset.service.service.impl.DepartmentServiceImpl;
import com.dataset.service.service.impl.EmployeeServiceImpl;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatasetServiceAutoConfiguration {

    @Bean
    public EmployeeService employeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        return new EmployeeServiceImpl(employeeRepository, departmentRepository);
    }

    @Bean
    public DepartmentService departmentService(DepartmentRepository departmentRepository) {
        return new DepartmentServiceImpl(departmentRepository);
    }

    @Bean(name = "dataSetServiceImpl")
    public DataSetService dataSetServiceImpl(EmployeeService employeeService, DepartmentService departmentService) {
        return new DataSetServiceImpl(employeeService, departmentService);
    }

    @Bean
    public SimpleModule recordDTOModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RecordDTO.class, new CustomRecordDTODeserializer());
        return module;
    }

    @Bean
    @Primary
    public DataSetService dataSetService(@Qualifier("dataSetServiceImpl") DataSetService dataSetService) {
        return new DataSetServiceProxy(dataSetService);
    }
}
