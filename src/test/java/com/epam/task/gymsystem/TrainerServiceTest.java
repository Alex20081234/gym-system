package com.epam.task.gymsystem;


import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.TrainingType;
import com.epam.task.gymsystem.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainerServiceTest {

    @Autowired
    private TrainerService service;

    @Test
    void testCreate() {
        Trainer expected = new Trainer("Steve", "Benson", 1L, new TrainingType("Fitness"));
        expected.setUsername("Steve.Benson");
        service.create(new Trainer("Steve", "Benson", 1L, new TrainingType("Fitness")));
        Trainer actual = service.select(1L);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
    }

    @Test
    void testUpdate() {
        Trainer expected = new Trainer("Kris", "Courtney", 2L, new TrainingType("Cardio"));
        expected.setUsername("Kris.Courtney");
        service.create(new Trainer("Roger", "Courtney", 2L, new TrainingType("Powerlifting")));
        service.update(2L, new Trainer("Kris", null, null, new TrainingType("Cardio")));
        Trainer actual = service.select(2L);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
    }

    @Test
    void testDelete() {
        service.create(new Trainer("Ron", "McDonald", 3L, new TrainingType("Gymnastics")));
        assertNotNull(service.select(3L));
        service.delete(3L);
        assertNull(service.select(3L));
    }
}
