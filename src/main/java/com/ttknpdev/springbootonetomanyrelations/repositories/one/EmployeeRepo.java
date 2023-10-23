package com.ttknpdev.springbootonetomanyrelations.repositories.one;

import com.ttknpdev.springbootonetomanyrelations.entities.one.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee,String> {

}
