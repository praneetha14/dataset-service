package com.dataset.service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/***
 * This is the generic response class for retrieving records from the dataset.
 * This record is used to return data for various query types — such as all records, grouped records, sorted records,
 * or a combination — along with an optional message.
 * @param message This is the success message for the specified query or creation.
 * @param sortedRecords This field holds the sorted records for the given dataset.
 * @param groupedRecords This field holds the grouped records for the given dataset.
 * @param records All records with no grouping and sorting.
 * @param <T>
 */
public record RecordResponseVO<T>(String message,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  T sortedRecords,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  T groupedRecords,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  T records) {
}
