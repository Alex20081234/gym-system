package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.TrainingType;
import com.epam.task.gymsystem.service.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainerServiceTest {
    @Autowired
    private TrainerServiceImpl service;

    @Test
    void createAndSelectTrainer() {
        Trainer trainer = Trainer.builder()
                .userId(1L)
                .firstName("Steve")
                .lastName("Benson")
                .specialization(TrainingType.builder().name("Fitness").build())
                .username("Steve.Benson")
                .build();
        service.create(trainer);
        assertTrue(service.select(1L).isPresent());
        assertEquals(trainer, service.select(1L).get());
    }

    @Test
    void selectNonExistentTrainer() {
        assertFalse(service.select(999L).isPresent());
    }

    @Test
    void updateTrainer() {
        Trainer trainer = Trainer.builder()
                .userId(2L)
                .firstName("Steve")
                .lastName("Benson")
                .specialization(TrainingType.builder().name("Fitness").build())
                .username("Steve.Benson")
                .build();
        service.create(trainer);
        trainer.setFirstName("John");
        service.update(2L, trainer);
        assertEquals("John", service.select(2L).get().getFirstName());
        assertEquals(trainer, service.select(2L).get());
    }
}
