package com.flywire.exercise;

import com.flywire.exercise.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FlywireSpringBootApp extends SpringBootServletInitializer implements CommandLineRunner {
    
    @Autowired
    private EmployeeService employeeService;

    public static void main(String[] args) {
        SpringApplication.run(FlywireSpringBootApp.class, args);
    }

    @Override
    public void run(String... args) {
        employeeService.initializeData();
    }
}
