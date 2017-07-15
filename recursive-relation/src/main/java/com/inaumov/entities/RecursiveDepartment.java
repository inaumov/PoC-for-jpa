package com.inaumov.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "department")
public class RecursiveDepartment {

    @Id
    @Column(name = "department_id")
    private String id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "name")
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "children",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<RecursiveDepartment> parents = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "department_parent",
            joinColumns = {
                    @JoinColumn(name = "parent_department_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
            })
    @OrderColumn(name = "department_order")
    private List<RecursiveDepartment> children = new ArrayList<>();

}