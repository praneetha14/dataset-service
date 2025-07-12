package com.dataset.service.model.response;

import java.util.UUID;

/***
 * This is the response class for returning the EmployeeVO(Employee view object) after transformation from entity layer.
 * @param id This is the unique identifier for employee record.
 * @param name This is the name of the employee.
 * @param departmentName This is the departmentName where employee works.
 * @param location This is the location associated with department.
 * @param age This is the age of employee.
 */
public record EmployeeVO(UUID id, String name, String departmentName, String location, int age) {
}
