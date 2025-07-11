package com.dataset.service.service.proxy;

import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.service.DataSetService;
import com.dataset.service.service.factory.DataSetFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class DataSetServiceProxy {

    private final DataSetFactory dataSetFactory;

    public RecordCreateResponseVO createRecord(RecordDTO recordDTO, String dataSetName) {
        isValidDataSet(dataSetName);
        DataSetService dataSetService = dataSetFactory.getService(dataSetName);
        return dataSetService.createRecord(recordDTO);
    }

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

    private void isValidDataSet(String dataSetName) {
        if (!SupportedDataSets.isSupportedDataSet(dataSetName)) {
            throw new RuntimeException("DataSet is not supported");
        }
    }
}
