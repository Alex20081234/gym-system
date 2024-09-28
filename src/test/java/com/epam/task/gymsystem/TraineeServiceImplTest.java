package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.service.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {
    @Mock
    private TraineeDaoImpl dao;
    @InjectMocks
    private TraineeServiceImpl service;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = Trainee.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("Trainee")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
    }

    @Test
    void testCreate() {
        doNothing().when(dao).create(any(Trainee.class));
        service.create(trainee);
        verify(dao, times(1)).create(trainee);
    }

    @Test
    void testChangePassword() throws UserNotFoundException {
        doNothing().when(dao).changePassword(anyString(), anyString());
        service.changePassword("Test.Trainee", "newpassword");
        verify(dao, times(1)).changePassword("Test.Trainee", "newpassword");
    }

    @Test
    void testUpdate() throws UserNotFoundException {
        Trainee updates = Trainee.builder()
                        .firstName("Updated")
                                .build();
        Trainee updated = Trainee.builder()
                .username("Updated.Trainee")
                .password("password")
                .firstName("Updated")
                .lastName("Trainee")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
        when(dao.select(anyString())).thenReturn(trainee);
        doNothing().when(dao).update(anyString(), any(Trainee.class));
        service.update("Test.Trainee", updates);
        verify(dao, times(1)).update("Test.Trainee", updated);
    }

    @Test
    void testChangeActivityStatus() throws UserNotFoundException, ActivityStatusAlreadyExistsException {
        doNothing().when(dao).changeActivityStatus(anyString(), anyBoolean());
        service.changeActivityStatus("Test.Trainee", false);
        verify(dao, times(1)).changeActivityStatus("Test.Trainee", false);
    }

    @Test
    void testSelectTrainings() throws UserNotFoundException {
        when(dao.selectTrainings(anyString(), any(LocalDate.class), any(LocalDate.class), any(), any())).thenReturn(List.of(new Training(), new Training()));
        List<Training> trainings = service.selectTrainings("Test.Trainee", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), null, null);
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(2, trainings.size());
    }

    @Test
    void testSelectNotAssignedTrainers() throws UserNotFoundException, NoExpectedDataInDatabaseException {
        when(dao.selectNotAssignedTrainers(anyString())).thenReturn(List.of(new Trainer()));
        List<Trainer> trainers = service.selectNotAssignedTrainers("Test.Trainee");
        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
    }

    @Test
    void testUpdateTrainers() throws UserNotFoundException {
        doNothing().when(dao).updateTrainers(anyString(), anyMap());
        service.updateTrainers("Test.Trainee", Map.of("Test.Trainer", true));
        verify(dao, times(1)).updateTrainers("Test.Trainee", Map.of("Test.Trainer", true));
    }

    @Test
    void testDelete() throws UserNotFoundException {
        doNothing().when(dao).delete(anyString());
        service.delete("Test.Trainee");
        verify(dao, times(1)).delete("Test.Trainee");
    }

    @Test
    void testSelect() throws UserNotFoundException {
        when(dao.select(anyString())).thenReturn(trainee);
        Trainee found = service.select("Test.Trainee");
        assertNotNull(found);
        assertEquals("Test.Trainee", found.getUsername());
    }

    @Test
    void testSelectAll() throws NoExpectedDataInDatabaseException {
        when(dao.selectAll()).thenReturn(List.of(trainee, new Trainee()));
        List<Trainee> trainees = service.selectAll();
        assertNotNull(trainees);
        assertFalse(trainees.isEmpty());
        assertEquals(2, trainees.size());
    }
}