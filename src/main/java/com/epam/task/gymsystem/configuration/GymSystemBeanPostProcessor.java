package com.epam.task.gymsystem.configuration;

import com.epam.task.gymsystem.domain.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
public class GymSystemBeanPostProcessor implements BeanPostProcessor {
    private static final String TRAINEE_STORAGE = "traineeStorage";
    private static final String TRAINER_STORAGE = "trainerStorage";
    private static final String TRAINING_STORAGE = "trainingStorage";
    @Value("${prepared.trainee.data.path}")
    private String traineeData;
    @Value("${prepared.trainer.data.path}")
    private String trainerData;
    @Value("${prepared.training.data.path}")
    private String trainingData;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return org.springframework.beans.factory.config.BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals(TRAINEE_STORAGE)) {
            Storage<Trainee> traineeStorage = (Storage<Trainee>) bean;
            processTraineeMap(traineeStorage.getMap());
            return traineeStorage;
        }
        if (beanName.equals(TRAINER_STORAGE)) {
            Storage<Trainer> trainerStorage = (Storage<Trainer>) bean;
            processTrainerMap(trainerStorage.getMap());
            return trainerStorage;
        }
        if (beanName.equals(TRAINING_STORAGE)) {
            Storage<Training> trainingStorage = (Storage<Training>) bean;
            processTrainingMap(trainingStorage.getMap());
            return trainingStorage;
        }
        return bean;
    }

    private void processTraineeMap(Map<Long, Trainee> map) {
        if (Files.exists(Path.of(traineeData))) {
            try {
                Map<Long, Trainee> data = objectMapper.readValue(Files.readAllBytes(Path.of(traineeData)), new TypeReference<>() {
                });
                map.putAll(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processTrainerMap(Map<Long, Trainer> map) {
        if (Files.exists(Path.of(trainerData))) {
            try {
                Map<Long, Trainer> data = objectMapper.readValue(Files.readAllBytes(Path.of(trainerData)), new TypeReference<>() {});
                map.putAll(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processTrainingMap(Map<Long, Training> map) {
        if (Files.exists(Path.of(trainingData))) {
            try {
                Map<Long, Training> data = objectMapper.readValue(Files.readAllBytes(Path.of(trainingData)), new TypeReference<>() {});
                map.putAll(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
