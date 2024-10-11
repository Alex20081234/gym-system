package com.epam.task.gymsystem;

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
import java.util.*;
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
    void createShouldTryToAddTraineeToDatabase() {
        doNothing().when(dao).create(any(Trainee.class));
        when(dao.selectUsernames()).thenReturn(Collections.emptyList());
        service.create(trainee);
        verify(dao, times(1)).create(trainee);
    }

    @Test
    void createShouldThrowIllegalArgumentExceptionWhenTraineeInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertEquals("Trainee is not valid", e.getMessage());
        trainee.setFirstName(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(trainee));
        assertEquals("Trainee is not valid", e.getMessage());
    }

    @Test
    void changePasswordShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changePassword(any(), anyString());
        doReturn(trainee).when(dao).select(anyString());
        service.changePassword("Test.Trainee", "newpassword");
        verify(dao, times(1)).changePassword("Test.Trainee", "newpassword");
    }

    @Test
    void changePasswordShouldThrowIllegalArgumentExceptionWhenPasswordInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainee", ""));
        assertEquals("New password is not valid", e.getMessage());
    }

    @Test
    void updateShouldTryToUpdateDatabase() {
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
        when(dao.update(any(), any(Trainee.class))).thenReturn("Updated.Trainee");
        when(dao.selectUsernames()).thenReturn(Collections.emptyList());
        service.update("Test.Trainee", updates);
        verify(dao, times(1)).update("Test.Trainee", updated);
    }

    @Test
    void updateShouldThrowIllegalArgumentExceptionWhenInvalidUpdates() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.update("Test.Trainer", null));
        assertEquals("Trainee is not valid", e.getMessage());
    }

    @Test
    void changeActivityStatusShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changeActivityStatus(any(), anyBoolean());
        doReturn(trainee).when(dao).select(anyString());
        service.changeActivityStatus("Test.Trainee", false);
        verify(dao, times(1)).changeActivityStatus("Test.Trainee", false);
    }

    @Test
    void changeActivityStatusShouldThrowIllegalArgumentExceptionWhenStatusIsAlreadyThat() {
        doReturn(trainee).when(dao).select(anyString());
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changeActivityStatus("Test.Trainee", true));
        assertEquals("Activity status is already true", e.getMessage());
    }

    @Test
    void selectTrainingsShouldTryToReturnTrainings() {
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
    void selectNotAssignedTrainersShouldTryToReturnTrainers() {
        when(dao.selectNotAssignedTrainers(any())).thenReturn(List.of(new Trainer()));
        when(dao.select(anyString())).thenReturn(trainee);
        List<Trainer> trainers = service.selectNotAssignedTrainers("Test.Trainee");
        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
    }

    @Test
    void updateTrainersShouldTryToUpdateDatabase() {
        doNothing().when(dao).updateTrainers(any(), anyMap());
        when(dao.select(anyString())).thenReturn(trainee);
        service.updateTrainers("Test.Trainee", Map.of("Test.Trainer", true));
        verify(dao, times(1)).updateTrainers(trainee, Map.of("Test.Trainer", true));
    }

    @Test
    void updateTrainersShouldThrowIllegalArgumentExceptionWhenTrainersInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.updateTrainers("Test.Trainee", null));
        assertEquals("Trainers are not valid", e.getMessage());
    }

    @Test
    void deleteShouldTryToDeleteTraineeFromDatabase() {
        doNothing().when(dao).delete(anyString());
        service.delete("Test.Trainee");
        verify(dao, times(1)).delete("Test.Trainee");
    }

    @Test
    void selectShouldTryToReturnTrainee() {
        trainee.setUsername("Test.Trainee");
        when(dao.select(anyString())).thenReturn(trainee);
        Trainee found = service.select("Test.Trainer");
        assertNotNull(found);
        assertEquals("Test.Trainee", found.getUsername());
    }

    @Test
    void selectAllShouldTryToReturnAllTrainees() {
        when(dao.selectAll()).thenReturn(List.of(trainee, new Trainee()));
        List<Trainee> trainees = service.selectAll();
        assertFalse(CollectionUtils.isEmpty(trainees));
        assertEquals(2, trainees.size());
    }
}