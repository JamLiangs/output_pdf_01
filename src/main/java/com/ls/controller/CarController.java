package com.ls.controller;

import com.ls.dao.CarDao;
import com.ls.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/pdf")
@Api(value = "PDF相关操作接口(Car)",tags = "PDF相关操作接口")
public class CarController {

    @Autowired
    private CarService carService;

    @ApiOperation(value = "导出PDF",httpMethod="POST")
    @PostMapping("/car")
    public void generatorCar(HttpServletResponse response){
        try{
            carService.generatorAdmissionCard(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
