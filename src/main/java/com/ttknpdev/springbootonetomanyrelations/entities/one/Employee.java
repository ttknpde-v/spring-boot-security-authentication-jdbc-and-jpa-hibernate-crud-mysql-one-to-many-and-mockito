package com.ttknpdev.springbootonetomanyrelations.entities.one;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    private String eid;
    private String fullname;
    private Short age;
    private String position;

    public Employee(String eid, String fullname, Short age, String position) {
        this.eid = eid;
        this.fullname = fullname;
        this.age = age;
        this.position = position;
    }
}
