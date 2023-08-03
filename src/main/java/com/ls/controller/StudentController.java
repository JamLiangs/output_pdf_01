package com.ls.controller;

import com.ls.domain.Student;
import com.ls.service.CarService;
import com.ls.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/pdf")
@Api(value = "PDF相关操作接口(Student)",tags = "PDF相关操作接口")
public class StudentController {

    @Autowired
    public StudentService studentService;

    private Logger logger = Logger.getLogger(StudentController.class);


    /*@ApiOperation(value = "导出PDF",httpMethod="POST")
    @PostMapping("/student")
    public void generatorStudent(@RequestBody Student student, HttpServletResponse response){
        try{
            studentService.generatorAdmissionCard(student,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
    @ApiOperation(value = "导出PDF",httpMethod="POST")
    @PostMapping("/student")
    public void generatorStudent(HttpServletResponse response){
        try{
            studentService.generatorAdmissionCard(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
