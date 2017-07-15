package com.inaumov.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@IdClass(DepartmentEmployeeId.class)
@Table(name = "department_employee")
@SecondaryTable(name = "employee",
        pkJoinColumns =
        @PrimaryKeyJoinColumn(name = "employee_id", referencedColumnName = "employee_id"),
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"employee_id", "name"})
)
public class DepartmentEmployee {

    @Id
    @Column(name = "employee_id")
    private String employeeId = UUID.randomUUID().toString();

    @Id
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "employee_order")
    @OrderBy
    private int orderingNumber;

    @Column(name = "active", table = "employee")
    private boolean active;

    @Column(name = "name", table = "employee")
    private String name;

    @OneToOne(targetEntity = Department.class, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

}