package com.dataset.service.service.proxy;

import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.factory.DataSetFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/***
 * DataSetServiceProxy is a proxy service for the Dataset service which holds all the validation before performing the
 * actual task in Dataset Service.
 */


@RequiredArgsConstructor
public class DataSetServiceProxy {

    /***
     * DataSetServiceProxy calls the DataSetFactory to resolve the actual DatasetService Bean i.e EmployeeDatasetService
     * or DepartmentDatasetService.
     */


    private final DataSetFactory dataSetFactory;

    /***
     * The method createRecord creates a record for a provided data set.
     * @param recordDTO This parameter holds the payload of the record that is to be created
     * @param dataSetName This parameter holds the name of the dataset for which a record is to be created
     * @return RecordCreateResponseVO which contains the id for the created resource followed by success message and dataset name
     */

    public RecordCreateResponseVO createRecord(RecordDTO recordDTO, String dataSetName) {
        isValidDataSet(dataSetName);
        DataSetService dataSetService = dataSetFactory.getService(dataSetName);
        return dataSetService.createRecord(recordDTO);
    }

    /***
     * Retrieves the records for the specified dataset with optional groupBy or sort By  or both operations
     * @param dataSetName This parameter holds the dataset name for which sortBy and groupBy operation is to be performed.
     * @param sortBy This parameter holds the name of the field on which sorting should be done.
     * @param groupBy This parameter holds the name of the field on which grouping should be done.
     * @param order This parameter holds the direction of the sort, i.e ASC or DESC
     * @return RecordResponseVO This method returns an object of RecordResponseVO which has some generic fields using
     * which the desired response is populated
     */

    public RecordResponseVO getSortedAndGroupedRecords(String dataSetName, String sortBy, String groupBy, String order) {
        isValidDataSet(dataSetName);
        DataSetService dataSetService = dataSetFactory.getService(dataSetName);
        if (StringUtils.hasText(groupBy) && StringUtils.hasText(sortBy)) {
            return dataSetService.getSortedAndGroupedRecords(sortBy, groupBy, order);
        } else if (StringUtils.hasText(groupBy)) {
            return dataSetService.getGroupedRecords(groupBy);
        } else if (StringUtils.hasText(sortBy)) {
            return dataSetService.getSortedRecords(sortBy, order);
        } else {
            return dataSetService.getAllRecords();
        }
    }

    /***
     * validates the dataset if it is present in the supportedDatSets enum
     * @param dataSetName This parameter holds the datasetName for which the validation is to be performed.
     */

    private void isValidDataSet(String dataSetName) {
        if (!SupportedDataSets.isSupportedDataSet(dataSetName)) {
            throw new RuntimeException("DataSet is not supported");
        }
    }
}
