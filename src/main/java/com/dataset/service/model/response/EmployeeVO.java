package com.dataset.service.model.response;

import java.util.UUID;

public record EmployeeVO(UUID id, String name, String departmentName, String location, int age) {
}
