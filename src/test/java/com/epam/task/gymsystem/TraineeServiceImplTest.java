package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import com.epam.task.gymsystem.service.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;
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
    @Mock
    private UserUtils userUtils;
    @InjectMocks
    private TraineeServiceImpl service;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = Trainee.builder()
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
    void create() {
        doNothing().when(dao).create(any(Trainee.class));
        doNothing().when(userUtils).setUsernameAndPassword(any(), anyList());
        trainee.setUsername("Test.Trainee");
        trainee.setPassword("1234567890");
        service.create(trainee);
        verify(dao, times(1)).create(trainee);
    }

    @Test
    void createThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        trainee.setDateOfBirth(null);
        assertThrows(IllegalArgumentException.class, () -> service.create(trainee));
    }

    @Test
    void changePassword() {
        doNothing().when(dao).changePassword(any(), anyString());
        doReturn(trainee).when(dao).select(anyString());
        service.changePassword("Test.Trainee", "newpassword");
        verify(dao, times(1)).changePassword(trainee, "newpassword");
    }

    @Test
    void changePasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainee", ""));
    }

    @Test
    void update() {
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
        doNothing().when(dao).update(any(), any(Trainee.class));
        when(userUtils.mergeUsers(any(), any(), anyList())).thenReturn(updated);
        service.update("Test.Trainee", updates);
        verify(dao, times(1)).update(trainee, updated);
    }

    @Test
    void updateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.update("Test.Trainer", null));
    }

    @Test
    void changeActivityStatus() {
        doNothing().when(dao).changeActivityStatus(any(), anyBoolean());
        doReturn(trainee).when(dao).select(anyString());
        service.changeActivityStatus("Test.Trainee", false);
        verify(dao, times(1)).changeActivityStatus(trainee, false);
    }

    @Test
    void changeActivityStatusThrowsException() {
        doReturn(trainee).when(dao).select(anyString());
        assertThrows(IllegalArgumentException.class, () -> service.changeActivityStatus("Test.Trainee", true));
    }

    @Test
    void selectTrainings() {
        when(dao.selectTrainings(any(), any())).thenReturn(List.of(new Training(), new Training()));
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(LocalDate.now().minusDays(1))
                .toDate(LocalDate.now().plusDays(1))
                .build();
        List<Training> trainings = service.selectTrainings("Test.Trainee", criteria);
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(2, trainings.size());
    }

    @Test
    void selectNotAssignedTrainers() {
        when(dao.selectNotAssignedTrainers(any())).thenReturn(List.of(new Trainer()));
        when(dao.select(anyString())).thenReturn(trainee);
        List<Trainer> trainers = service.selectNotAssignedTrainers("Test.Trainee");
        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
    }

    @Test
    void updateTrainers() {
        doNothing().when(dao).updateTrainers(any(), anyMap());
        when(dao.select(anyString())).thenReturn(trainee);
        service.updateTrainers("Test.Trainee", Map.of("Test.Trainer", true));
        verify(dao, times(1)).updateTrainers(trainee, Map.of("Test.Trainer", true));
    }

    @Test
    void updateTrainersThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateTrainers("Test.Trainee", null));
    }

    @Test
    void delete() {
        doNothing().when(dao).delete(anyString());
        service.delete("Test.Trainee");
        verify(dao, times(1)).delete("Test.Trainee");
    }

    @Test
    void select() {
        trainee.setUsername("Test.Trainee");
        when(dao.select(anyString())).thenReturn(trainee);
        Trainee found = service.select("Test.Trainer");
        assertNotNull(found);
        assertEquals("Test.Trainee", found.getUsername());
    }

    @Test
    void selectAll() {
        when(dao.selectAll()).thenReturn(List.of(trainee, new Trainee()));
        List<Trainee> trainees = service.selectAll();
        assertFalse(CollectionUtils.isEmpty(trainees));
        assertEquals(2, trainees.size());
    }
}