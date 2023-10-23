package com.ttknpdev.springbootonetomanyrelations.controller;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import com.ttknpdev.springbootonetomanyrelations.entities.one.Employee;
import com.ttknpdev.springbootonetomanyrelations.service.many.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/car")
public class ControllerPathCar {
    private final CarService carService;

    @Autowired
    public ControllerPathCar(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(value = "/reads")
    private ResponseEntity<?> reads() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(carService.reads());
    }

    @GetMapping(value = "/reads/{eid}/employee")
    private ResponseEntity<?> readsByEmployeeID(@PathVariable String eid) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(carService.readsByEmployeeID(eid));
    }

    @GetMapping(value = "/read/{engin}")
    private ResponseEntity<?> reads(@PathVariable String engin) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(carService.read(engin));
    }

    @PostMapping(value = "/{eid}/create")
    private ResponseEntity<?> create(@PathVariable String eid,@RequestBody Car car) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(carService.create(car,eid)
                );
    }
    @PutMapping(value = "/update/{engine}")
    private ResponseEntity<?> update(@PathVariable String engine , @RequestBody Car car) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(carService.update(engine,car)
                );
    }

    @DeleteMapping(value = "/delete/{eid}/employee")
    private ResponseEntity<?> deleteAllByEmployeeId(@PathVariable String eid) {
        return ResponseEntity
                .status(HttpStatus.OK).body(carService.deleteAllByEmployeeID(eid));
    }
    @DeleteMapping(value = "/delete/{eid}")
    private ResponseEntity<?> delete(@PathVariable String eid) {
        return ResponseEntity
                .status(HttpStatus.OK).body(carService.delete(eid));
    }
}
