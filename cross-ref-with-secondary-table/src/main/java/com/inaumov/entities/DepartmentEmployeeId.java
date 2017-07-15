package com.inaumov.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class DepartmentEmployeeId implements Serializable {

    private String departmentId;
    private String employeeId;

    public DepartmentEmployeeId() {
    }

    public DepartmentEmployeeId(String departmentId, String employeeId) {
        this.departmentId = departmentId;
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DepartmentEmployeeId that = (DepartmentEmployeeId) o;
        return Objects.equals(this.departmentId, that.departmentId)
                && Objects.equals(this.employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.departmentId, this.employeeId);
    }

}