package com.ls.dao;

import com.ls.domain.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarDao {
    
    @Select("select * from studenttopdfnew;")
    public List<Car> getAll();
}
