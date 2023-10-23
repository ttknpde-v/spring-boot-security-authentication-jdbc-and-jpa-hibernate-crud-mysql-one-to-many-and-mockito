package com.ttknpdev.springbootonetomanyrelations.entities.many;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttknpdev.springbootonetomanyrelations.entities.one.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor

@Entity
@Table(name = "cars")
public class Car {
    private String cid;
    @Id
    @Column(name = "engine_number")
    private String engineNumber;
    private String brand;
    private String model;
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eid" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    /*
    -- the @JoinColumn annotation to specify the foreign key column (eid).
    -- If you don’t provide the JoinColumn name, the name will be set automatically.

    -- @JsonIgnore is used to ignore the logical property used in serialization and deserialization.

    -- We also implement cascade delete capabilities of the foreign-key with @OnDelete(action = OnDeleteAction.CASCADE)
    and this is result
    about fetch = FetchType.LAZY
    {
        "cid": "C001",
        "engineNumber": "KN0023LLPK",
        "brand": "Ford",
        "model": "Ranger XLS",
        "price": 899000.0
    }
    , ...

    about fetch = FetchType.EAGER and close @JsonIgnore
    {
        "cid": "C001",
        "engineNumber": "KN0023LLPK",
        "brand": "Ford",
        "model": "Ranger XLS",
        "price": 899000.0,
        "employee": {
            "eid": "E001",
            "fullname": "Peter Parker",
            "age": 23,
            "position": "back-end"
        }
    }
    */
    private Employee employee;

    public Car(String cid, String engineNumber, String brand, String model, Double price) {
        this.cid = cid;
        this.engineNumber = engineNumber;
        this.brand = brand;
        this.model = model;
        this.price = price;
    }
}
