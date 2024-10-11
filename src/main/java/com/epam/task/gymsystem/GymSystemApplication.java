package com.epam.task.gymsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GymSystemApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(GymSystemApplication.class, args);
    }
}
