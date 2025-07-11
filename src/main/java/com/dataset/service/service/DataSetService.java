package com.dataset.service.service;

import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordResponseVO;

public interface DataSetService {
    RecordCreateResponseVO createRecord(RecordDTO recordDTO);
    RecordResponseVO getGroupedRecords(String groupBy);
    RecordResponseVO getSortedRecords(String groupBy, String order);
    RecordResponseVO getSortedAndGroupedRecords(String sortBy, String groupBy, String order);
    RecordResponseVO getAllRecords();
}
