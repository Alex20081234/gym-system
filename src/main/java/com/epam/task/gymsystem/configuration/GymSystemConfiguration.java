package com.epam.task.gymsystem.configuration;

import com.epam.task.gymsystem.domain.Storage;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Random;

@Configuration
public class GymSystemConfiguration {
    @Bean
    public Storage<Trainee> traineeStorage() {
        return new Storage<>();
    }

    @Bean
    public Storage<Trainer> trainerStorage() {
        return new Storage<>();
    }

    @Bean
    public Storage<Training> trainingStorage() {
        return new Storage<>();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
