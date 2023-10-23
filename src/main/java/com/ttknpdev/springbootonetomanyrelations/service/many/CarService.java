package com.ttknpdev.springbootonetomanyrelations.service.many;


import java.util.Map;

public interface CarService <T> {
    Iterable<T> reads();
    Iterable<T> readsByEmployeeID(String eid);
    T read(String engineNumber);
    T update(String engineNumberDefault , T obj);
    T create (T obj , String eid);
    T createForTest (T obj , String eid);
    Iterable<T>  deleteAllByEmployeeID(String eid);
    Map<String , T> delete(String engineNumberDefault);
}
