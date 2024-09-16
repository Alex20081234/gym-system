package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.Storage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GymSystemBeanPostProcessorTest {
    @Autowired
    private Storage<Trainee> traineeStorage;
    @Autowired
    private Storage<Trainer> trainerStorage;
    @Autowired
    private Storage<Training> trainingStorage;

    @Test
    void testProcessTraineeMap() {
        assertFalse(traineeStorage.getMap().isEmpty());
        assertTrue(traineeStorage.getMap().values().stream()
                .anyMatch(trainee -> "Test".equals(trainee.getFirstName()) && "Trainee".equals(trainee.getLastName())));
    }

    @Test
    void testProcessTrainerMap() {
        assertFalse(trainerStorage.getMap().isEmpty());
        assertTrue(trainerStorage.getMap().values().stream()
                .anyMatch(trainer -> "Test".equals(trainer.getFirstName()) && "Trainer".equals(trainer.getLastName())));
    }

    @Test
    void testProcessTrainingMap() {
        assertFalse(trainingStorage.getMap().isEmpty());
        assertTrue(trainingStorage.getMap().values().stream()
                .anyMatch(training -> "Test Training".equals(training.getTrainingName())));
    }
}