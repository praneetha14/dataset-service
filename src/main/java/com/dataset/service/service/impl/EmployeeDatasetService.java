package com.dataset.service.service.impl;

import com.dataset.service.entity.DepartmentEntity;
import com.dataset.service.entity.EmployeeEntity;
import com.dataset.service.model.request.EmployeeDTO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.EmployeeVO;
import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.repository.EmployeeRepository;
import com.dataset.service.service.DataSetService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EmployeeDatasetService implements DataSetService {

    private static final Set<String> DEPARTMENT_FIELDS = Set.of("departmentName", "location");
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EntityManager entityManager;

    private EmployeeEntity toEntity(EmployeeDTO employeeDTO, DepartmentEntity departmentEntity) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setAge(employeeDTO.getAge());
        employeeEntity.setDepartment(departmentEntity);
        return employeeEntity;
    }

    @Override
    public RecordCreateResponseVO createRecord(RecordDTO recordDTO) {
        EmployeeDTO employeeDTO = (EmployeeDTO) recordDTO;
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findById(employeeDTO.getDepartmentId());
        if (optionalDepartmentEntity.isEmpty()) {
            throw new RuntimeException("Department not found with id: " + employeeDTO.getDepartmentId());
        }
        EmployeeEntity employeeEntity = toEntity(employeeDTO, optionalDepartmentEntity.get());
        employeeEntity = employeeRepository.save(employeeEntity);
        return new RecordCreateResponseVO("Record created successfully", "Employee_dataset", employeeEntity.getId());
    }

    @Override
    public RecordResponseVO getGroupedRecords(String groupBy) {
        List<EmployeeVO> employeeVOS = employeeRepository.findAllEmployeeDetails();
        Map<String, List<EmployeeVO>> data = groupAndGetData(employeeVOS, groupBy);
        return new RecordResponseVO("Grouped data successfully", null, data, null);
    }

    @Override
    public RecordResponseVO getSortedRecords(String sortBy, String order) {
        List<EmployeeVO> employeeVOS = sortAndGetData(sortBy, order);
        return new RecordResponseVO("Fetched sorted records successfully", employeeVOS, null, null);
    }

    @Override
    public RecordResponseVO getSortedAndGroupedRecords(String sortBy, String groupBy, String order) {
        List<EmployeeVO> employeeVOS = sortAndGetData(sortBy, order);
        Map<String, List<EmployeeVO>> data = groupAndGetData(employeeVOS, groupBy);
        return new RecordResponseVO("Fetched sorted and grouped records successfully", null, data, null);
    }

    @Override
    public RecordResponseVO getAllRecords() {
        return new RecordResponseVO("Fetched All records successfully", null, null,
                employeeRepository.findAllEmployeeDetails());
    }

    private String getFormattedSortByFieldForDepartmentFields(String sortBy) {
        if (DEPARTMENT_FIELDS.contains(sortBy)) {
            if ("departmentName".equals(sortBy)) {
                return "department.name";
            } else {
                return "department." + sortBy;
            }
        } else if ("id".equalsIgnoreCase(sortBy)) {
            return "e.id";
        } else {
            return sortBy;
        }
    }

    private Map<String, List<EmployeeVO>> groupAndGetData(List<EmployeeVO> employeeVOS, String groupBy) {
        return employeeVOS.stream()
                .collect(Collectors.groupingBy(employee -> {
                    try {
                        Field field = employee.getClass().getDeclaredField(groupBy);
                        field.setAccessible(true);
                        Object value = field.get(employee);
                        return value != null ? value.toString() : "null";
                    } catch (Exception ex) {
                        throw new RuntimeException("Error while grouping records", ex);
                    }
                }));
    }

    private List<EmployeeVO> sortAndGetData(String sortBy, String order) {
        String orderBy = getFormattedSortByFieldForDepartmentFields(sortBy);

        String jpaQuery = "SELECT new com.dataset.service.model.response.EmployeeVO(" +
                "e.id, e.name, d.name, d.location, e.age) " +
                "FROM EmployeeEntity e " +
                "JOIN e.department d " +
                "ORDER BY " + orderBy + " " + order;
        return (List<EmployeeVO>) entityManager.createQuery(jpaQuery).getResultList();
    }
}
