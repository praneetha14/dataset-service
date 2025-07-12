package com.dataset.service.model.response;

import java.util.UUID;

/***
 * This is the RecordCreateResponseVO class which creates a record for the provided dataset.
 * @param message This is the success message displays after creating record.
 * @param dataset This is name of the dataset to which the record was added.
 * @param id This is the unique identifier assigned to newly created record.
 */
public record RecordCreateResponseVO(String message, String dataset, UUID id) {
}
