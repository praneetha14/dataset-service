package com.dataset.service.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/***
 * EmployeeDTO is the payload or the request object for creating a Employee Record.
 */

@Getter
@Setter
public class EmployeeDTO implements RecordDTO {
    private String name;
    private int age;
    private UUID departmentId;
}
