package com.dataset.service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record RecordResponseVO<T>(String message,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  T sortedRecords,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  T groupedRecords,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  T records) {
}
