package com.epam.gymsystem.service;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.dao.TrainerDao;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {
    @Mock
    private TrainerDao dao;
    @Mock
    private PasswordEncoder encoder;
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
        when(encoder.encode(anyString())).thenReturn("password");
        service.create(trainer);
        verify(dao, times(1)).create(trainer);
    }

    @Test
    void createShouldThrowIllegalArgumentExceptionWhenTrainerInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertEquals("Trainer is not valid", e.getMessage());
        when(dao.selectUsernames()).thenReturn(Collections.emptyList());
        when(encoder.encode(anyString())).thenReturn("password");
        trainer.setIsActive(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(trainer));
        assertEquals("Trainer is not valid", e.getMessage());
        trainer.setIsActive(true);
        when(dao.selectUsernames()).thenReturn(Collections.emptyList());
        when(encoder.encode(anyString())).thenReturn("password");
        trainer.setSpecialization(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(trainer));
        assertEquals("Trainer is not valid", e.getMessage());
    }

    @Test
    void changePasswordShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changePassword(any(), anyString());
        doReturn(Optional.of(trainer)).when(dao).select(anyString());
        when(encoder.encode(anyString())).thenReturn("newpassword");
        service.changePassword("Test.Trainer", "newpassword");
        verify(dao, times(1)).changePassword("Test.Trainer", "newpassword");
    }

    @Test
    void changePasswordShouldThrowIllegalArgumentExceptionWhenPasswordInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainer", null));
        assertEquals("New password is not valid", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainer", "12"));
        assertEquals("New password is not valid", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainer", "01234567890123456789028484545"));
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
        for (int i = 0; i < 2; i++) {
            when(dao.select(anyString())).thenReturn(Optional.of(trainer));
            when(encoder.encode("password")).thenReturn("password");
            when(dao.update(any(), any(Trainer.class))).thenReturn("Updated.Trainer");
            when(dao.selectUsernames()).thenReturn(Collections.emptyList());
            service.update("Test.Trainer", updates);
            verify(dao, times(i + 1)).update("Test.Trainer", updated);
            updates.setPassword("password");
        }
    }

    @Test
    void updateShouldThrowIllegalArgumentExceptionWhenInvalidUpdates() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.update("Test.Trainer", null));
        assertEquals("Trainer is not valid", e.getMessage());
    }

    @Test
    void updateShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> service.update("Non.Existent", new Trainer()));
        assertEquals("Trainer with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void changeActivityStatusShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changeActivityStatus(any(), anyBoolean());
        doReturn(Optional.of(trainer)).when(dao).select(anyString());
        service.changeActivityStatus("Test.Trainer", false);
        verify(dao, times(1)).changeActivityStatus("Test.Trainer", false);
    }

    @Test
    void changeActivityStatusShouldThrowIllegalArgumentExceptionWhenStatusIsAlreadyThat() {
        doReturn(Optional.of(trainer)).when(dao).select(anyString());
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changeActivityStatus("Test.Trainer", true));
        assertEquals("Activity status is already true", e.getMessage());
    }

    @Test
    void changeActivityStatusShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> service.changeActivityStatus("Non.Existent", true));
        assertEquals("Trainer with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void selectShouldTryToReturnTrainer() {
        trainer.setUsername("Test.Trainer");
        when(dao.select(anyString())).thenReturn(Optional.of(trainer));
        Trainer found = service.select("Test.Trainer").orElse(null);
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

    @Test
    void loadDependencies() {
        doNothing().when(dao).loadDependencies(trainer);
        service.loadDependencies(trainer);
        verify(dao, times(1)).loadDependencies(trainer);
    }
}
