package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import com.epam.task.gymsystem.service.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainingServiceTest {
    @Autowired
    private TrainingServiceImpl service;

    @Test
    void createAndSelectTraining() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 9, 18, 17, 0, 0);
        Training training = Training.builder()
                .trainingId(1L)
                .trainerId(2L)
                .traineeId(3L)
                .trainingName("Cardio on 18th of September")
                .trainingType(TrainingType.builder().name("Cardio").build())
                .trainingDate(dateTime)
                .duration(Duration.ofMinutes(90))
                .build();
        service.create(training);
        assertTrue(service.select(1L).isPresent());
        assertEquals(training, service.select(1L).get());
    }

    @Test
    void selectNonExistentTraining() {
        assertFalse(service.select(999L).isPresent());
    }
}