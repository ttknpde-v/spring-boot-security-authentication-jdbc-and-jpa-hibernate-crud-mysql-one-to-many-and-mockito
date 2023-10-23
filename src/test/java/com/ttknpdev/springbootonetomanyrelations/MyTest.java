package com.ttknpdev.springbootonetomanyrelations;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import com.ttknpdev.springbootonetomanyrelations.entities.one.Employee;
import com.ttknpdev.springbootonetomanyrelations.repositories.many.CarRepo;
import com.ttknpdev.springbootonetomanyrelations.repositories.one.EmployeeRepo;
import com.ttknpdev.springbootonetomanyrelations.service.many.CarImplement;
import com.ttknpdev.springbootonetomanyrelations.service.many.CarService;
import com.ttknpdev.springbootonetomanyrelations.service.one.EmployeeImplement;
import com.ttknpdev.springbootonetomanyrelations.service.one.EmployeeService;
// import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;


// import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// concept is mocking
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MyTest {
    /*
    The @Mock annotation creates a mock implementation for the class it is annotated with.
    The @InjectMocks also creates the mock implementation of annotated type and injects the dependent mocks into it.
    */
    // mock (v. จำลอง , ล้อเลียน)
    // (extraInterfaces = {EmployeeService.class})
    @Mock
    private EmployeeRepo employeeRepo;
    @Mock
    private CarRepo carRepo;
    private EmployeeService<Employee> employeeService;
    private CarService<Car> carService;
    @BeforeEach
    public void initUseCase() {
        employeeService = new EmployeeImplement(employeeRepo);
        carService = new CarImplement(carRepo,employeeRepo);
    }

    @Test
    public void createEmployee() {
        when(employeeRepo.save( any(Employee.class)) ).then(returnsFirstArg());
        Employee newEmployee = employeeService.create(getEmployee());
        assertEquals("Peter Parker",newEmployee.getFullname());
        verify(employeeRepo,times(1)).save(getEmployee()); // verify (v. ตรวจสอบ) , invocations (n. การร้องขอ)
    }

    @Test
    public void readsEmployee() {
        when(employeeRepo.findAll()).thenReturn(getEmployees());
        List<Employee> employees = (List<Employee>) employeeService.reads();
        assertEquals("Peter Parker",employees.get(0).getFullname());
        verify(employeeRepo,times(1)).findAll(); // verify (v. ตรวจสอบ) , invocations (n. การร้องขอ)
    }

    @Test
    public void readEmployee() {
        String eid = "E001";
        when(employeeRepo.findById(eid)).thenReturn(Optional.of(getEmployee()));
        Employee employee = (Employee) employeeService.read(eid);
        assertEquals("Peter Parker",employee.getFullname());
        verify(employeeRepo,times(1)).findById(eid); // verify (v. ตรวจสอบ) , invocations (n. การร้องขอ)
    }


    @Test
    public void createCar() {
        when(carRepo.save( any(Car.class)) ).then(returnsFirstArg());
        Car newCar = carService.createForTest(getCar(),"E001");
        assertEquals("KN0023LLPK",newCar.getEngineNumber());
        verify(carRepo,times(1)).save(getCar()); // verify (v. ตรวจสอบ) , invocations (n. การร้องขอ)
    }

    @Test
    public void readsCar() {
        when(carRepo.findAll()).thenReturn(getCars());
        List<Car> cars = (List<Car>) carService.reads();
        assertEquals("KN0023LLPK",cars.get(0).getEngineNumber());
        verify(carRepo,times(1)).findAll(); // verify (v. ตรวจสอบ) , invocations (n. การร้องขอ)
    }

    private List<Employee> getEmployees() {
        /*
        List<User> expected = new ArrayList<>();
        User a = new User("adam","adam@abc.abc");
        User b = new User("kavin","kavin@abc.abc");
        expected.add(a);
        expected.add(b);
        */
        return List.of(
                new Employee("E001","Peter Parker",(short)30,"Back-end"),
                new Employee("E002","Alex Parker",(short)31,"Back-end")
        );
    }
    private List<Car> getCars () {
        return List.of(
                new Car("C001","KN0023LLPK","Ford","Ranger XLS",89900.0),
                new Car("C002","KN0023LLPD","Ford","Ranger XL+",79900.0)
        );
    }
    private Employee getEmployee() {
        return new Employee("E001","Peter Parker",(short)30,"Back-end");
    }
    private Car getCar() {
        return new Car("C001","KN0023LLPK","Ford","Ranger XLS",89900.0);
    }
}
