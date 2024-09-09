package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import com.epam.task.gymsystem.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrainingServiceTest {

    @Autowired
    private TrainingService service;

    @Test
    void testCreate() {
        ZonedDateTime dateTime = ZonedDateTime.of(2024, 9, 18, 17, 0, 0, 0, ZoneId.systemDefault());
        Training expected = new Training(1L, 2L, 3L, "Cardio on 18th of september", new TrainingType("Cardio"), Date.from(dateTime.toInstant()), Duration.ofMinutes(90));
        service.create(new Training(1L, 2L, 3L, "Cardio on 18th of september", new TrainingType("Cardio"), Date.from(dateTime.toInstant()), Duration.ofMinutes(90)));
        Training actual = service.select(1L);
        assertEquals(expected, actual);
    }


}
