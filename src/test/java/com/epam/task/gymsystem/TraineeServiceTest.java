package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TraineeServiceTest {

    @Autowired
    private TraineeService service;

    @Test
    void testCreate() {
        ZonedDateTime dateTime = ZonedDateTime.of(1980, 4, 10, 0, 0, 0, 0, ZoneId.systemDefault());
        Trainee expected = new Trainee("Bob", "Robinson", Date.from(dateTime.toInstant()), "Newline Avenue, New York", 1L);
        expected.setUsername("Bob.Robinson");
        service.create(new Trainee("Bob", "Robinson", Date.from(dateTime.toInstant()), "Newline Avenue, New York", 1L));
        Trainee actual = service.select(1L);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    void testUpdate() {
        ZonedDateTime dateTime = ZonedDateTime.of(1979, 6, 11, 0, 0, 0, 0, ZoneId.systemDefault());
        Trainee expected = new Trainee("Kyle", "Smith", Date.from(dateTime.toInstant()), "Old Town, Rome", 2L);
        expected.setUsername("Kyle.Smith");
        service.create(new Trainee("Kyle", "Robinson", Date.from(dateTime.toInstant()), "Newline Avenue, New York", 2L));
        service.update(2L, new Trainee(null, "Smith", null, "Old Town, Rome", null));
        Trainee actual = service.select(2L);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    void testDelete() {
        ZonedDateTime dateTime = ZonedDateTime.of(1979, 6, 11, 0, 0, 0, 0, ZoneId.systemDefault());
        service.create(new Trainee("Ben", "Stilton", Date.from(dateTime.toInstant()), "Reading, London", 3L));
        assertNotNull(service.select(3L));
        service.delete(3L);
        assertNull(service.select(3L));
    }


}
