package com.epam.task.gymsystem.configuration;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
public class GymSystemBeanPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor {

    @Value("${prepared.trainee.data.path}")
    private String traineeData;

    @Value("${prepared.trainer.data.path}")
    private String trainerData;

    @Value("${prepared.training.data.path}")
    private String trainingData;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return org.springframework.beans.factory.config.BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Map) {
            try {
                Map<Long, Trainee> map = (Map<Long, Trainee>) bean;
                processTraineeMap(map);
                return map;
            } catch (ClassCastException e) {
                try {
                    Map<Long, Trainer> map = (Map<Long, Trainer>) bean;
                    processTrainerMap(map);
                    return map;
                } catch (ClassCastException ignored) {}
            }
            Map<Long, Training> map = (Map<Long, Training>) bean;
            processTrainingMap(map);
            return map;
        }
        return bean;
    }

    private void processTraineeMap(Map<Long, Trainee> map) {
        if (Files.exists(Path.of(traineeData))) {
            //logic for populating map with some data from a file according to file's type
        }
    }

    private void processTrainerMap(Map<Long, Trainer> map) {
        if (Files.exists(Path.of(trainerData))) {
            //logic for populating map with some data from a file according to file's type
        }
    }

    private void processTrainingMap(Map<Long, Training> map) {
        if (Files.exists(Path.of(trainingData))) {
            //logic for populating map with some data from a file according to file's type
        }
    }
}
