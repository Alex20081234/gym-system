package com.epam.task.gymsystem.configuration;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configuration {


    @Bean
    public Map<Long, Trainee> traineeMap() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerMap() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingMap() {
        return new HashMap<>();
    }
}
