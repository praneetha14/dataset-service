package com.dataset.service.restcontroller;

import com.dataset.service.model.RecordCreateResponseVO;
import com.dataset.service.model.RecordDTO;
import com.dataset.service.service.DataSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
public class DataSetController {
    private final DataSetService dataSetService;

    @PostMapping("/{datasetName}/record")
    public ResponseEntity<RecordCreateResponseVO> createRecord(@PathVariable("datasetName") String datasetName,
                                                               @RequestBody RecordDTO recordDTO) {
        return new ResponseEntity<>(dataSetService.createRecord(recordDTO, datasetName), HttpStatus.CREATED);
    }
}
