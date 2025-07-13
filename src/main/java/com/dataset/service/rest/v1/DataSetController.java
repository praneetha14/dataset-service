package com.dataset.service.rest.v1;

import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * DataSetController handles operations related to datasets and their records.
 * <p>
 * This controller provides two REST endpoints in which 1st endpoint is for creating record based on input dataset provided.
 * 2nd endpoint is to query the dataset record based on grouping and sorting criteria.
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
     * @param datasetName This is the name of dataset in which the record should be created.
     * @param recordDTO The payload containing the record details.
     * @return ResponseEntity containing the result of record creation.
     */


    @PostMapping("/{datasetName}/record")

    public ResponseEntity<RecordCreateResponseVO> createRecord(
            @PathVariable("datasetName") String datasetName,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "create record payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Employee Record",
                                            summary = "An example of an Employee record payload",
                                            value = "{ \"name\": \"Alice Admin\", \"age\": 35, "
                                                    + "\"departmentId\": \"8dff2c8a-121c-4a2e-97c7-4c26ccc549bd\" }"
                                    ),
                                    @ExampleObject(
                                            name = "Department Record",
                                            summary = "An example of a Department payload",
                                            value = "{ \"name\": \"Bob User\", \"location\": \"Bangalore\" }"
                                    )
                            }
                    )
            ) RecordDTO recordDTO) {
        return new ResponseEntity<>(dataSetServiceProxy.createRecord(recordDTO, datasetName), HttpStatus.CREATED);
    }

    /***
     * Queries the list of records for the specified dataset, with optional groupBy, sortBy operations
     * @param datasetName This is the datasetName to which query is performed.
     * @param groupBy This contains the name of the field to perform groupBy.
     * @param sortBy This contains the name of the field to perform sortBy.
     * @param order This is the order i.e. ASC or DESC.
     * @return RecordResponseVO which contains the groupBy or sortBy records.
     */

    @GetMapping("/{datasetName}/query")
    public ResponseEntity<RecordResponseVO> groupByQuery(@PathVariable String datasetName,
                                                         @RequestParam(required = false, defaultValue = "departmentName") String groupBy,
                                                         @RequestParam(required = false, defaultValue = "age") String sortBy,
                                                         @RequestParam(required = false, defaultValue = "ASC")
                                                         String order) {
        return ResponseEntity.ok(dataSetServiceProxy.getSortedAndGroupedRecords(datasetName, sortBy, groupBy, order));
    }
}
