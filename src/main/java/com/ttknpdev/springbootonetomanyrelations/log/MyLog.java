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
