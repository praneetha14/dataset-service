package com.dataset.service.service.proxy;

import com.dataset.service.model.RecordCreateResponseVO;
import com.dataset.service.model.RecordDTO;
import com.dataset.service.model.enums.SupportedDataSets;
import com.dataset.service.service.DataSetService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataSetServiceProxy implements DataSetService {

    private final DataSetService dataSetService;

    @Override
    public RecordCreateResponseVO createRecord(RecordDTO recordDTO, String dataSetName) {
        isValidDataSet(dataSetName);
        return dataSetService.createRecord(recordDTO, dataSetName);
    }

    private void isValidDataSet(String dataSetName) {
        if (!SupportedDataSets.isSupportedDataSet(dataSetName)) {
            throw new RuntimeException("DataSet is not supported");
        }
    }
}
