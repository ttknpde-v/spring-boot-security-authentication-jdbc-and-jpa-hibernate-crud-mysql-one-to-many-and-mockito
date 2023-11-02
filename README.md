# Spring boot security crud mysql (one to many)
<details>
  
  - Authentication by jdbc
  - Using jpa/hibernate 
  - Using mockito
  - Using log4j

</details>

Developed by [ttknpde-v](https://github.com/ttknpde-v)

# What things I got this project

- [x] How to set up log4j pattern on properties file
<details>
  
`log4j.properties`
```properties
  # root logger option
  log4j.rootLogger=DEBUG,console
  
  # redirect log output to console
  log4j.appender.console=org.apache.log4j.ConsoleAppender
  log4j.appender.console.Target=System.out
  log4j.appender.console.layout=org.apache.log4j.PatternLayout
  log4j.appender.console.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} [%t] %-5p %c{1}:%L - %m%n
  
  # mark
  # %d{yyyy-MM-dd HH:mm:ss} = Date and time format
  # %-5p = The logging priority, like DEBUG or ERROR. The -5 is optional, for the pretty print format.
  # %c{1} = The logging name we set via getLogger()
  # %L = The line number from where the logging request.
  # %m%n = The message to log and line break.

```
`MyLog.java`
```java

  package com.ttknpdev.springbootonetomanyrelations.log;

  import com.ttknpdev.springbootonetomanyrelations.secure.ConfigSecure;
  import com.ttknpdev.springbootonetomanyrelations.service.many.CarImplement;
  import com.ttknpdev.springbootonetomanyrelations.service.one.EmployeeImplement;
  import org.apache.log4j.Logger;

  public interface MyLog {
      Logger employeeImplement = Logger.getLogger(EmployeeImplement.class);
      Logger carImplement = Logger.getLogger(CarImplement.class);
      Logger configSecure = Logger.getLogger(ConfigSecure.class);
  }

```

</details>

- [x] Understand to use @ManyToOne annotation
<details>
  
 `example's inside entity Car`
```java
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eid" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Employee employee;
    /*
    -- The @JoinColumn annotation to specify the foreign key column (eid).
    -- If you don’t provide the JoinColumn name, the name will be set automatically.
    -- @JsonIgnore is used to ignore the logical property used in serialization and deserialization.
    -- We also implement cascade delete capabilities of the foreign-key with @OnDelete(action = OnDeleteAction.CASCADE)
    And this is result
    about fetch = FetchType.LAZY
    {
        "cid": "C001",
        "engineNumber": "KN0023LLPK",
        "brand": "Ford",
        "model": "Ranger XLS",
        "price": 899000.0
    }
    , ...

    About fetch = FetchType.EAGER and close @JsonIgnore
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
```
  
</details>

- [x] Understand more how to use security in spring
<details>
  
`These are dependency for security and authentication`
  
 ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
```

 `This way give you how to config security & authenticate` 
  
  ```java
  package com.ttknpdev.springbootonetomanyrelations.secure;

  import com.ttknpdev.springbootonetomanyrelations.log.MyLog;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.http.HttpMethod;
  import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
  import org.springframework.security.web.SecurityFilterChain;
  
  import javax.sql.DataSource;
  
  @Configuration
  @EnableWebSecurity // need to use for any method that using @Autowire annotation
  public class ConfigSecure {

      /* An instance of DataSource object will be created and injected by Spring framework */
      private final DataSource dataSource;
  
      @Autowired
      public ConfigSecure(DataSource dataSource) {
          /*
             When CDI is working
             It will read database connection information from the application.properties file.
          */
          MyLog.configSecure.info("ConfigSecure constructor's class is working");
          this.dataSource = dataSource;
      }

      @Autowired
      public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
          /*
              As you can see, we need to specify a password encoder (BCrypt is recommended),
              data source and two SQL statements: the first one selects a user based on username, and the second one selects role of the user.
              Note that Spring security requires the column names must be username, password, enabled and role.
          */
          MyLog.configSecure.info("configAuthentication(AuthenticationManagerBuilder) method is working");
          BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
          auth.jdbcAuthentication()
                  .passwordEncoder(bCryptPasswordEncoder)
                  .dataSource(dataSource)
                  /*
                     two SQL statements: the first one selects a user based on username,
                     and the second one selects role of the user.
                  */
                  // Note that Spring security requires the column names must be username, password, enabled and role.
                  .usersByUsernameQuery("select username , password , enabled from register where username = ?")
                  .authoritiesByUsernameQuery("select username , role from register where username = ?");
      }

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http.csrf().disable()
                  .authorizeHttpRequests((authorize) ->
                          authorize.requestMatchers(HttpMethod.GET,"/api/employee/reads").hasRole("EMPLOYEE")
                                  .requestMatchers(HttpMethod.GET,"/api/employee/read/**").hasAnyRole("EMPLOYEE","USER")
                                  .requestMatchers(HttpMethod.POST,"/api/employee/create").hasRole("EMPLOYEE")
                                  .requestMatchers(HttpMethod.PUT,"/api/employee/update/**").hasRole("EMPLOYEE")
                                  .requestMatchers(HttpMethod.DELETE,"/api/employee/delete/**").hasRole("EMPLOYEE")
                                  /*
                                     That is Worked !!!!  It will check role in database of username ?
                                     And it cuts ROLE_ ?
                                  */
                                  .requestMatchers(HttpMethod.POST,"/api/car/**").hasAnyRole("EMPLOYEE","USER")
                                  .requestMatchers(HttpMethod.GET,"/api/car/reads").hasRole("EMPLOYEE")
                                  .requestMatchers(HttpMethod.GET,"/api/car/reads/**").hasRole("EMPLOYEE")
                                  .requestMatchers(HttpMethod.GET,"/api/car/read/**").hasAnyRole("EMPLOYEE","USER")
                                  .requestMatchers(HttpMethod.PUT,"/api/car/update/**").hasRole("EMPLOYEE")
                                  .requestMatchers(HttpMethod.DELETE,"/api/car/delete/**").hasRole("EMPLOYEE")
                                  .anyRequest().authenticated()
                  )
                  .formLogin()
                  .and()
                  .logout().permitAll();
          http.httpBasic(); // for allowed to send  basic auth on Post Man
          return http.build();
      }

  }
```

</details>

- [x] Understand more how to use native query by jpa
<details>

```java
package com.ttknpdev.springbootonetomanyrelations.repositories.many;

import com.ttknpdev.springbootonetomanyrelations.entities.many.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface CarRepo extends JpaRepository<Car,String> {
    @Query(value = "select * from cars where eid = :eid" , nativeQuery = true)
    List<Car> readByEmployeeId(@Param("eid") String eid);
    @Query(value = "delete from cars where eid = :eid" , nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAllByEmployeeId(@Param("eid") String eid);
}

```
  
</details>

- [x] Testing this application with mokito [example](https://github.com/ttknpde-v/spring-boot-security-authentication-jdbc-and-jpa-hibernate-crud-mysql-one-to-many-and-mockito/blob/main/src/test/java/com/ttknpdev/springbootonetomanyrelations/MyTest.java)
<details>
  
  `these are dependency for using mockito`

```xml
   <dependency>
     <groupId>org.junit.jupiter</groupId>
     <artifactId>junit-jupiter-api</artifactId>
     <version>5.8.1</version>
     <scope>test</scope>
   </dependency>
   <dependency>
     <groupId>org.junit.jupiter</groupId>
     <artifactId>junit-jupiter-engine</artifactId>
     <version>5.8.1</version>
     <scope>test</scope>
   </dependency>
```
  
</details>

# My Description

Use jdbc dependency for path authentication and build some one to many relation (two tables) for concept crud 

I also keep testing with mockito/junit. 

I use @ManyToOne for entity that can have many , because it is the most appropriate way
