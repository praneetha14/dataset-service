package com.dataset.service.service;

import com.dataset.service.model.RecordCreateResponseVO;
import com.dataset.service.model.RecordDTO;

public interface DataSetService {
    RecordCreateResponseVO createRecord(RecordDTO recordDTO, String dataSetName);
}
