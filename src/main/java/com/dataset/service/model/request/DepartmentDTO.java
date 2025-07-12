package com.dataset.service.model.request;

import lombok.Getter;
import lombok.Setter;
/***
 * DepartmentDTO is the payload or the request object for creating a Department Record.
 */

@Getter
@Setter
public class DepartmentDTO implements RecordDTO {
    private String departmentName;
    private String location;
}
