package com.epam.gymsystem.service;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.dao.TraineeDaoImpl;
import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {
    @Mock
    private TraineeDaoImpl dao;
    @Mock
    private PasswordEncoder encoder;
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
        when(encoder.encode(anyString())).thenReturn("password");
        service.create(trainee);
        verify(dao, times(1)).create(trainee);
    }

    @Test
    void createShouldThrowIllegalArgumentExceptionWhenTraineeInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertEquals("Trainee is not valid", e.getMessage());
        trainee.setIsActive(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(trainee));
        assertEquals("Trainee is not valid", e.getMessage());
    }

    @Test
    void changePasswordShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changePassword(any(), anyString());
        doReturn(Optional.of(trainee)).when(dao).select(anyString());
        when(encoder.encode(anyString())).thenReturn("newpassword");
        service.changePassword("Test.Trainee", "newpassword");
        verify(dao, times(1)).changePassword("Test.Trainee", "newpassword");
    }

    @Test
    void changePasswordShouldThrowIllegalArgumentExceptionWhenPasswordInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainee", null));
        assertEquals("New password is not valid", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainer", "12"));
        assertEquals("New password is not valid", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> service.changePassword("Test.Trainer", "01234567890123456789028484545"));
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
        when(dao.select(anyString())).thenReturn(Optional.of(trainee));
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
    void updateShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> service.update("Non.Existent", new Trainee()));
        assertEquals("Trainee with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void changeActivityStatusShouldTryToMakeChangeToDatabase() {
        doNothing().when(dao).changeActivityStatus(any(), anyBoolean());
        doReturn(Optional.of(trainee)).when(dao).select(anyString());
        service.changeActivityStatus("Test.Trainee", false);
        verify(dao, times(1)).changeActivityStatus("Test.Trainee", false);
    }

    @Test
    void changeActivityStatusShouldThrowIllegalArgumentExceptionWhenStatusIsAlreadyThat() {
        doReturn(Optional.of(trainee)).when(dao).select(anyString());
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.changeActivityStatus("Test.Trainee", true));
        assertEquals("Activity status is already true", e.getMessage());
    }

    @Test
    void changeActivityStatusShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> service.changeActivityStatus("Non.Existent", true));
        assertEquals("Trainee with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void selectNotAssignedTrainersShouldTryToReturnTrainers() {
        when(dao.selectNotAssignedTrainers(any())).thenReturn(List.of(new Trainer()));
        when(dao.select(anyString())).thenReturn(Optional.of(trainee));
        List<Trainer> trainers = service.selectNotAssignedTrainers("Test.Trainee");
        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
    }

    @Test
    void selectNotAssignedTrainersShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> service.selectNotAssignedTrainers("Non.Existent"));
        assertEquals("Trainee with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void updateTrainersShouldTryToUpdateDatabase() {
        doNothing().when(dao).updateTrainers(any(), anyMap());
        when(dao.select(anyString())).thenReturn(Optional.of(trainee));
        service.updateTrainers("Test.Trainee", Map.of("Test.Trainer", true));
        verify(dao, times(1)).updateTrainers(trainee, Map.of("Test.Trainer", true));
    }

    @Test
    void updateTrainersShouldThrowIllegalArgumentExceptionWhenTrainersInvalid() {
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.updateTrainers("Test.Trainee", null));
        assertEquals("Trainers are not valid", e.getMessage());
    }

    @Test
    void updateTrainersShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> service.updateTrainers("Non.Existent", new HashMap<>()));
        assertEquals("Trainee with username Non.Existent was not found", e.getMessage());
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
        when(dao.select(anyString())).thenReturn(Optional.of(trainee));
        Trainee found = service.select("Test.Trainer").orElse(null);
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

    @Test
    void loadDependencies() {
        doNothing().when(dao).loadDependencies(trainee);
        service.loadDependencies(trainee);
        verify(dao, times(1)).loadDependencies(trainee);
    }
}
