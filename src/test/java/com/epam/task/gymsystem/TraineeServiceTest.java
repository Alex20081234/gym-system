package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.service.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TraineeServiceTest {
    @Autowired
    private TraineeServiceImpl service;

    @Test
    void createAndSelectTrainee() {
        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .build();
        service.create(trainee);
        assertTrue(service.select(1L).isPresent());
        assertEquals(trainee, service.select(1L).get());
    }

    @Test
    void selectNonExistentTrainee() {
        assertFalse(service.select(999L).isPresent());
    }

    @Test
    void updateTrainee() {
        Trainee trainee = Trainee.builder()
                .userId(2L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .address("1234 Elm St")
                .dateOfBirth(LocalDateTime.of(1990, 1, 1, 0, 0))
                .build();
        service.create(trainee);
        trainee.setFirstName("Jane");
        service.update(2L, trainee);
        assertEquals("Jane", service.select(2L).get().getFirstName());
        assertEquals(trainee, service.select(2L).get());
    }

    @Test
    void deleteTrainee() {
        Trainee trainee = Trainee.builder()
                .userId(3L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .build();
        service.create(trainee);
        service.delete(3L);
        assertFalse(service.select(3L).isPresent());
    }
}
