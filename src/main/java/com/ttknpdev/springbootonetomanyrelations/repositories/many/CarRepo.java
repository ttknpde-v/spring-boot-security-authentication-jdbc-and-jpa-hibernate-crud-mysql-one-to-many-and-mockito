package com.ttknpdev.springbootonetomanyrelations.repositories.many;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface CarRepo extends JpaRepository<Car,String> {
    @Query(value = "select * from cars where eid = :eid" , nativeQuery = true)
    List<Car> readByEmployeeId(@Param("eid") String eid);
    @Query(value = "delete from cars where eid = :eid" , nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAllByEmployeeId(@Param("eid") String eid);
}
