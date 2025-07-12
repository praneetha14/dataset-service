package com.dataset.service.model.response;

import java.util.UUID;

/***
 * This is the response class for returning the DepartmentVO.
 * @param id This is the unique identifier for the department record.
 * @param name This is the name of the department
 * @param location This is the location of department where it is located.
 */
public record DepartmentVO(UUID id, String name, String location) {
}
