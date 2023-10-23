package com.ttknpdev.springbootonetomanyrelations.service.one;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import com.ttknpdev.springbootonetomanyrelations.entities.one.Employee;
import com.ttknpdev.springbootonetomanyrelations.log.MyLog;
import com.ttknpdev.springbootonetomanyrelations.repositories.one.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeImplement implements EmployeeService<Employee> {
    private EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeImplement(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Iterable<Employee> reads() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee read(String eid) {
        return employeeRepo.findById(eid).orElse(new Employee());
    }

    @Override
    public Employee create(Employee obj ) {
        // validate basic
        MyLog.employeeImplement.info("didn't validate");
        return employeeRepo.save(obj);
    }

    @Override
    public Employee update(Employee obj , String eid) {
        return employeeRepo.findById(eid).map(employee -> {
            MyLog.carImplement.info("The "+obj.getEid()+" have existed (can update)");
            employee.setAge(obj.getAge());
            employee.setFullname(obj.getFullname());
            employee.setPosition(obj.getPosition());
            return employeeRepo.save(employee);
        }).orElse(new Employee());
    }

    @Override
    public Map<String, Employee> delete(String eid) {
        Map<String , Employee> response = new HashMap<>();
        employeeRepo.findById(eid).ifPresent(employee -> {
            response.put("deleted",employee);
            employeeRepo.delete(employee);
        });
        return response;
    }
}
