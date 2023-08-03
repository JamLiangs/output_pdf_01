package com.ls.dao;

import com.ls.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper
public interface StudentDao {

    @Select("select * from studenttopdf")
    public List<Student> getAll();



}
