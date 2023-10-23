package com.ttknpdev.springbootonetomanyrelations.controller;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import com.ttknpdev.springbootonetomanyrelations.entities.one.Employee;
import com.ttknpdev.springbootonetomanyrelations.repositories.one.EmployeeRepo;
import com.ttknpdev.springbootonetomanyrelations.service.one.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/employee")
public class ControllerPathEmployee {
    private EmployeeService employeeService;
    @Autowired
    public ControllerPathEmployee(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping(value = "/reads")
    private ResponseEntity<?> reads() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.reads()
                );
    }
    @GetMapping(value = "/read/{eid}")
    private ResponseEntity<?> read(@PathVariable String eid) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.read(eid)
                );
    }
    @PostMapping(value = "/create")
    private ResponseEntity<?> create(@RequestBody Employee employee) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.create(employee)
                );
    }

    @PutMapping(value = "/update/{eid}")
    private ResponseEntity<?> update(@RequestBody Employee employee , @PathVariable String eid) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.update(employee,eid)
                );
    }

    @DeleteMapping(value = "/delete/{eid}")
    private ResponseEntity<?> delete(@PathVariable String eid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.delete(eid)
                );
    }
}
