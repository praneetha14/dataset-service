package com.dataset.service.service.impl;

import com.dataset.service.entity.DepartmentEntity;
import com.dataset.service.model.enums.SupportedDataSets;
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

/***
 * DepartmentDatasetService is a concrete implementation of DatasetService interface and provides the
 * implementation of methods related to createRecord and searchRecord for Department dataset.
 */


@RequiredArgsConstructor
public class DepartmentDatasetService implements DataSetService {

    private final DepartmentRepository departmentRepository;

    private final EntityManager entityManager;

    /***
     * Converts DepartmentDTO to departmentEntity
     * @param departmentDTO
     * @return
     */

    private DepartmentEntity toEntity(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setName(departmentDTO.getDepartmentName());
        departmentEntity.setLocation(departmentDTO.getLocation());
        return departmentEntity;
    }

    /***
     * creates a new department record
     * @param recordDTO
     * @return
     */

    @Override
    public RecordCreateResponseVO createRecord(RecordDTO recordDTO) {
        DepartmentDTO departmentDTO = (DepartmentDTO) recordDTO;
        DepartmentEntity departmentEntity = toEntity(departmentDTO);
        departmentEntity = departmentRepository.save(departmentEntity);
        return new RecordCreateResponseVO("Record created successfully", SupportedDataSets.DEPARTMENT.getValue(),
                departmentEntity.getId());
    }

    /***
     * Groups department records by the given field
     * @param groupBy
     * @return
     */

    @Override
    public RecordResponseVO getGroupedRecords(String groupBy) {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        List<DepartmentVO> departmentVOS = toVO(departmentEntities);
        Map<String, List<DepartmentVO>> data = groupAndGetData(departmentVOS, groupBy);
        return new RecordResponseVO("Fetched grouped records successfully", null, data, null);
    }

    /***
     * Sort the department records by given field and order
     * @param sortBy
     * @param order
     * @return
     */

    @Override
    public RecordResponseVO getSortedRecords(String sortBy, String order) {
        return new RecordResponseVO("Fetched sorted records successfully",
                sortAndGetData(sortBy, order), null, null);
    }

    /***
     * Sort the records and then group the records by given order
     * @param sortBy
     * @param groupBy
     * @param order
     * @return
     */

    @Override
    public RecordResponseVO getSortedAndGroupedRecords(String sortBy, String groupBy, String order) {
        List<DepartmentVO> departmentVOS = sortAndGetData(sortBy, order);
        Map<String, List<DepartmentVO>> data = groupAndGetData(departmentVOS, groupBy);
        return new RecordResponseVO("Fetched sorted and grouped records successfully", null, data,
                null);
    }

    /***
     * Fetches all the department records without orderBy and sortBy
     * @return
     */

    @Override
    public RecordResponseVO getAllRecords() {
        return new RecordResponseVO("Fetched all records successfully", null, null,
                toVO(departmentRepository.findAll()));
    }

    /***
     * Converts the list of entities to list of value objects
     * @param departmentEntities
     * @return
     */

    private List<DepartmentVO> toVO(List<DepartmentEntity> departmentEntities) {
        return departmentEntities.stream()
                .map(departmentEntity -> new DepartmentVO(
                        departmentEntity.getId(),
                        departmentEntity.getName(),
                        departmentEntity.getLocation()))
                .toList();
    }

    /***
     * Groups the records dynamically by the given field using reflection
     * @param departmentVOS
     * @param groupBy
     * @return
     */

    private Map<String, List<DepartmentVO>> groupAndGetData(List<DepartmentVO> departmentVOS, String groupBy) {
        return departmentVOS.stream().collect(Collectors.groupingBy(department -> {
            try {
                Field field = department.getClass().getDeclaredField(groupBy);
                field.setAccessible(true);
                Object value = field.get(department);
                return value != null ? value.toString() : "null";
            } catch (Exception e) {
                throw new RuntimeException("Error getting grouped records", e);
            }
        }));
    }

    /***
     * Sorts department data dynamically using JpaQuery
     * @param sortBy
     * @param order
     * @return
     */

    private List<DepartmentVO> sortAndGetData(String sortBy, String order) {
        String jpaQuery = "select new com.dataset.service.model.response.DepartmentVO(d.id, d.name, d.location)  "
                + "from DepartmentEntity d order by " + sortBy + " " + order;
        return (List<DepartmentVO>) entityManager.createQuery(jpaQuery).getResultList();
    }
}
