package com.dataset.service.service;

import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordResponseVO;

/***
 * DataSetService is the interface for the service layer of this application
 * This class is responsible for creating records based on given dataset and query the list of records for given fields
 * for sortBy and groupBy query
 */

public interface DataSetService {
    RecordCreateResponseVO createRecord(RecordDTO recordDTO);
    RecordResponseVO getGroupedRecords(String groupBy);
    RecordResponseVO getSortedRecords(String groupBy, String order);
    RecordResponseVO getSortedAndGroupedRecords(String sortBy, String groupBy, String order);
    RecordResponseVO getAllRecords();
}
