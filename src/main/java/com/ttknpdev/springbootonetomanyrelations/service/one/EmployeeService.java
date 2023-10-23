package com.ttknpdev.springbootonetomanyrelations.service.one;

import java.util.Map;

public interface EmployeeService <T>{
    Iterable<T> reads();
    T read(String eid);
    T create(T obj);
    T update(T obj , String eid);
    Map<String , T> delete(String eid);

}
