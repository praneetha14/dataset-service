package com.dataset.service.restcontroller;

import com.dataset.service.model.response.RecordCreateResponseVO;
import com.dataset.service.model.request.RecordDTO;
import com.dataset.service.model.response.RecordResponseVO;
import com.dataset.service.service.proxy.DataSetServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
public class DataSetController {
    private final DataSetServiceProxy dataSetServiceProxy;

    @PostMapping("/{datasetName}/record")
    public ResponseEntity<RecordCreateResponseVO> createRecord(@PathVariable("datasetName") String datasetName,
                                                               @RequestBody RecordDTO recordDTO) {
        return new ResponseEntity<>(dataSetServiceProxy.createRecord(recordDTO, datasetName), HttpStatus.CREATED);
    }

    @GetMapping("/{datasetName}/query")
    public ResponseEntity<RecordResponseVO> groupByQuery(@PathVariable String datasetName,
                                                         @RequestParam(required = false) String groupBy,
                                                         @RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false, defaultValue = "ASC")
                                                         String order) {
        return ResponseEntity.ok(dataSetServiceProxy.getSortedAndGroupedRecords(datasetName, sortBy, groupBy, order));
    }
}
