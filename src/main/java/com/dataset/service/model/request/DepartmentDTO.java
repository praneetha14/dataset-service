package com.dataset.service.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDTO implements RecordDTO {
    private String departmentName;
    private String location;
}
