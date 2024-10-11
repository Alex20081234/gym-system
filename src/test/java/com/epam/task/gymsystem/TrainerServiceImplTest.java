package com.epam.task.gymsystem;

import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import com.epam.task.gymsystem.domain.TrainingType;
import com.epam.task.gymsystem.service.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {
    @Mock
    private TrainerDaoImpl dao;
    @InjectMocks
    private TrainerServiceImpl service;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainer = Trainer.builder()
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .specialization(new TrainingType())
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
    }

    @Test
    void createShouldTryToAddTrainerToDatabase() {
        doNothing().when(dao).create(any(Trainer.class));
        when(dao.selectUsernames()).thenReturn(Collections.emptyList());
        service.create(trainer);
        verify(dao, times(1)).create(trainer);
    }

    @Test
    void createShouldThrowIllegalArgumentExceptionWhenTrainerInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertEquals("Trainer is not valid", e.getMessage());
        trainer.setSpecialization(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(trainer));
        assertEquals("Trainer is not valid", e.getMessage());
    }

    @Test
    void changePasswordShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changePassword(any(), anyString());
        doReturn(trainer).when(dao).select(anyString());
        service.changePassword("Test.Trainer", "newpassword");
        verify(dao, times(1)).changePassword("Test.Trainer", "newpassword");
    }

    @Test
    void changePasswordShouldThrowIllegalArgumentExceptionWhenPasswordInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainer", ""));
        assertEquals("New password is not valid", e.getMessage());
    }

    @Test
    void updateShouldTryToUpdateDatabase() {
        Trainer updates = Trainer.builder()
                .firstName("Updated")
                .build();
        Trainer updated = Trainer.builder()
                .username("Updated.Trainer")
                .password("password")
                .firstName("Updated")
                .lastName("Trainer")
                .isActive(true)
                .specialization(null)
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        when(dao.select(anyString())).thenReturn(trainer);
        when(dao.update(any(), any(Trainer.class))).thenReturn("Updated.Trainer");
        when(dao.selectUsernames()).thenReturn(Collections.emptyList());
        service.update("Test.Trainer", updates);
        verify(dao, times(1)).update("Test.Trainer", updated);
    }

    @Test
    void updateShouldThrowIllegalArgumentExceptionWhenInvalidUpdates() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.update("Test.Trainer", null));
        assertEquals("Trainer is not valid", e.getMessage());
    }

    @Test
    void changeActivityStatusShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changeActivityStatus(any(), anyBoolean());
        doReturn(trainer).when(dao).select(anyString());
        service.changeActivityStatus("Test.Trainer", false);
        verify(dao, times(1)).changeActivityStatus("Test.Trainer", false);
    }

    @Test
    void changeActivityStatusShouldThrowIllegalArgumentExceptionWhenStatusIsAlreadyThat() {
        doReturn(trainer).when(dao).select(anyString());
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changeActivityStatus("Test.Trainer", true));
        assertEquals("Activity status is already true", e.getMessage());
    }

    @Test
    void selectTrainingsShouldTryToReturnTrainings() {
        when(dao.selectTrainings(any(), any())).thenReturn(List.of(new Training(), new Training()));
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(LocalDate.now().minusDays(1))
                .toDate(LocalDate.now().plusDays(1))
                .build();
        List<Training> trainings = service.selectTrainings("Test.Trainer", criteria);
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(2, trainings.size());
    }

    @Test
    void selectShouldTryToReturnTrainer() {
        trainer.setUsername("Test.Trainer");
        when(dao.select(anyString())).thenReturn(trainer);
        Trainer found = service.select("Test.Trainer");
        assertNotNull(found);
        assertEquals("Test.Trainer", found.getUsername());
    }

    @Test
    void selectAllShouldTryToReturnAllTrainers() {
        when(dao.selectAll()).thenReturn(List.of(trainer, new Trainer()));
        List<Trainer> trainers = service.selectAll();
        assertFalse(CollectionUtils.isEmpty(trainers));
        assertEquals(2, trainers.size());
    }
}
