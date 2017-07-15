package com.inaumov.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Department {

    @Id
    @Column(name = "department_id")
    private String departmentId = UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn(name = "department_id", referencedColumnName = "department_id")
    private List<DepartmentEmployee> departmentEmployees = new ArrayList<DepartmentEmployee>();

    @Override
    public String toString() {
        return "Department{" +
                "departmentId='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}