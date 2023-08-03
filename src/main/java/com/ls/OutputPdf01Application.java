package com.ls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class OutputPdf01Application {

    public static void main(String[] args) {
        SpringApplication.run(OutputPdf01Application.class, args);
    }

}
