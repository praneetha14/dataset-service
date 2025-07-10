package com.dataset.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmployeeDTO implements RecordDTO {
    private String name;
    private int age;
    private UUID departmentId;
}
