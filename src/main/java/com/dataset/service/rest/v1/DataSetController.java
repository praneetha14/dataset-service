package com.dataset.service.rest.v1;

import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *  DataSetController handles operations related to datasets and their records.
 *
 *  This controller provides two REST endpoints in which 1st endpoint is for creating record based on input dataset provided.
 *  2nd endpoint is to query the dataset record based on grouping and sorting criteria.
 */


@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
public class DataSetController {

    /***
     *  DataSetServiceProxy is responsible for processing dataset related operations
     */

    private final DataSetServiceProxy dataSetServiceProxy;

    /***
     * Creates a record for the specified dataset.
     *
     * @param datasetName
     * @param recordDTO
     * @return
     */


    @PostMapping("/{datasetName}/record")
    public ResponseEntity<RecordCreateResponseVO> createRecord(@PathVariable("datasetName") String datasetName,
                                                               @RequestBody RecordDTO recordDTO) {
        return new ResponseEntity<>(dataSetServiceProxy.createRecord(recordDTO, datasetName), HttpStatus.CREATED);
    }

    /***
     * Queries the list of records for the specified dataset, with optional groupBy, sortBy operations
     * @param datasetName
     * @param groupBy
     * @param sortBy
     * @param order
     * @return
     */

    @GetMapping("/{datasetName}/query")
    public ResponseEntity<RecordResponseVO> groupByQuery(@PathVariable String datasetName,
                                                         @RequestParam(required = false) String groupBy,
                                                         @RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false, defaultValue = "ASC")
                                                         String order) {
        return ResponseEntity.ok(dataSetServiceProxy.getSortedAndGroupedRecords(datasetName, sortBy, groupBy, order));
    }
}
