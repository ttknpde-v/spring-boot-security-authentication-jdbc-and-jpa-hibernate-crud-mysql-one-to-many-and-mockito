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
