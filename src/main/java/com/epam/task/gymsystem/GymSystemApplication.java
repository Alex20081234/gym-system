package com.epam.task.gymsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class GymSystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(GymSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GymSystemApplication.class, args);
        logger.info("Gym system application has started successfully");
    }

}
