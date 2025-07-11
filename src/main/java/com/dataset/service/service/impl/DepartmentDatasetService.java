package com.dataset.service.service.impl;

import com.dataset.service.entity.DepartmentEntity;
import com.dataset.service.model.request.DepartmentDTO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.DepartmentVO;
import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.repository.DepartmentRepository;
import com.dataset.service.service.DataSetService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DepartmentDatasetService implements DataSetService {

    private final DepartmentRepository departmentRepository;

    private final EntityManager entityManager;

    private DepartmentEntity toEntity(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setName(departmentDTO.getDepartmentName());
        departmentEntity.setLocation(departmentDTO.getLocation());
        return departmentEntity;
    }

    @Override
    public RecordCreateResponseVO createRecord(RecordDTO recordDTO) {
        DepartmentDTO departmentDTO = (DepartmentDTO) recordDTO;
        DepartmentEntity departmentEntity = toEntity(departmentDTO);
        departmentEntity = departmentRepository.save(departmentEntity);
        return new RecordCreateResponseVO("Record added successfully", "Department_dataset", departmentEntity.getId());
    }

    @Override
    public RecordResponseVO getGroupedRecords(String groupBy) {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        List<DepartmentVO> departmentVOS = toVO(departmentEntities);
        Map<String, List<DepartmentVO>> data = groupAndGetData(departmentVOS, groupBy);
        return new RecordResponseVO("Fetched grouped records successfully", null, data, null);
    }

    @Override
    public RecordResponseVO getSortedRecords(String sortBy, String order) {
        return new RecordResponseVO("Fetched sorted records successfully",
                sortAndGetData(sortBy, order), null, null);
    }

    @Override
    public RecordResponseVO getSortedAndGroupedRecords(String sortBy, String groupBy, String order) {
        List<DepartmentVO> departmentVOS = sortAndGetData(sortBy, order);
        Map<String, List<DepartmentVO>> data = groupAndGetData(departmentVOS, groupBy);
        return new RecordResponseVO("Fetched sorted and grouped records successfully", null, data,
                null);
    }

    @Override
    public RecordResponseVO getAllRecords() {
        return new RecordResponseVO("Fetched all records successfully", null, null,
                toVO(departmentRepository.findAll()));
    }

    private List<DepartmentVO> toVO(List<DepartmentEntity> departmentEntities) {
        return departmentEntities.stream()
                .map(departmentEntity -> new DepartmentVO(
                        departmentEntity.getId(),
                        departmentEntity.getName(),
                        departmentEntity.getLocation()))
                .toList();
    }

    private Map<String, List<DepartmentVO>> groupAndGetData(List<DepartmentVO> departmentVOS, String groupBy) {
        return departmentVOS.stream().collect(Collectors.groupingBy(department -> {
            try {
                Field field = department.getClass().getField(groupBy);
                field.setAccessible(true);
                Object value = field.get(department);
                return value != null ? value.toString() : "null";
            } catch (Exception e) {
                throw new RuntimeException("Error getting grouped records", e);
            }
        }));
    }

    private List<DepartmentVO> sortAndGetData(String sortBy, String order) {
        String jpaQuery = "select new com.dataset.service.model.response.DepartmentVO(d.id, d.name, d.location)  "
                + "from DepartmentEntity d order by " + sortBy + " " + order;
        return (List<DepartmentVO>) entityManager.createQuery(jpaQuery).getResultList();
    }
}
