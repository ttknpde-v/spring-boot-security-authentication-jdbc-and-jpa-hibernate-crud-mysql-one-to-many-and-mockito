package com.ttknpdev.springbootonetomanyrelations.service.many;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import com.ttknpdev.springbootonetomanyrelations.log.MyLog;
import com.ttknpdev.springbootonetomanyrelations.repositories.many.CarRepo;
import com.ttknpdev.springbootonetomanyrelations.repositories.one.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class CarImplement implements CarService<Car> {
    private CarRepo carRepo;
    private EmployeeRepo employeeRepo;

    @Autowired
    public CarImplement(CarRepo carRepo, EmployeeRepo employeeRepo) {
        this.carRepo = carRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Iterable<Car> reads() {
        return carRepo.findAll();
    }

    @Override
    public Iterable<Car> readsByEmployeeID(String eid) {
        return carRepo.readByEmployeeId(eid);
    }

    @Override
    public Car read(String engineNumber) {
        // Car clone = new Car();
        return carRepo.findById(engineNumber).orElse(new Car());
    }

    @Override
    public Car create(Car obj , String eid) {
        /*
        *
        * Comment comment = tutorialRepository.findById(tutorialId).map(tutorial -> {
          commentRequest.setTutorial(tutorial);
          return commentRepository.save(commentRequest);
        }*/
        return employeeRepo.findById(eid).map(employee -> {
            /*
            select e1_0.eid,e1_0.age,e1_0.fullname,e1_0.position
            from employees e1_0
            where e1_0.eid=?

            */
            MyLog.carImplement.info("The "+eid+" have existed (can create & join)");
            /*
            The E002 have existed
            */
            obj.setEmployee(employee);
            /*
            select c1_0.engine_number,c1_0.brand,c1_0.cid,c1_0.eid,
                   e1_0.eid,e1_0.age,e1_0.fullname,e1_0.position,
                   c1_0.model,c1_0.price
                   from cars c1_0
                   join employees
                   e1_0 on e1_0.eid = c1_0.eid
                   where c1_0.engine_number=?
            */
            return carRepo.save(obj);
            /* insert into cars (brand,cid,eid,model,price,engine_number) values (?,?,?,?,?,?) */
        }).orElse(
                // if the eid haven't existed
                new Car()
        );
    }
    // For testing
    @Override
    public Car createForTest(Car obj, String eid) {
        if (eid.equals("E001")) {
            return carRepo.save(obj);
        }
        return null;
    }

    @Override
    public Car update(String engineNumberDefault, Car obj) {
        return carRepo.findById(engineNumberDefault)
                .map(car -> {
                    car.setBrand(obj.getBrand());
                    car.setPrice(obj.getPrice());
                    car.setModel(obj.getModel());
                    // car.setCid(obj.getCid());
                    return carRepo.save(car);
                }).orElse(new Car());
    }

    @Override
    public Iterable<Car> deleteAllByEmployeeID(String eid) {
        carRepo.deleteAllByEmployeeId(eid);
        return carRepo.findAll();
    }

    @Override
    public Map<String, Car> delete(String engineNumberDefault) {
        Map<String , Car> response = new HashMap<>();
        carRepo.findById(engineNumberDefault).ifPresent(car -> {
            response.put("deleted",car);
            carRepo.delete(car);
        });
        return response;
    }
}
