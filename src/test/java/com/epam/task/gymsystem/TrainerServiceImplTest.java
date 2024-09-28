package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.service.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
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
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .specialization(null)
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
    }

    @Test
    void testCreate() {
        doNothing().when(dao).create(any(Trainer.class));
        service.create(trainer);
        verify(dao, times(1)).create(trainer);
    }

    @Test
    void testChangePassword() throws UserNotFoundException {
        doNothing().when(dao).changePassword(anyString(), anyString());
        service.changePassword("Test.Trainer", "newpassword");
        verify(dao, times(1)).changePassword("Test.Trainer", "newpassword");
    }

    @Test
    void testUpdate() throws UserNotFoundException {
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
        doNothing().when(dao).update(anyString(), any(Trainer.class));
        service.update("Test.Trainer", updates);
        verify(dao, times(1)).update("Test.Trainer", updated);
    }

    @Test
    void testChangeActivityStatus() throws UserNotFoundException, ActivityStatusAlreadyExistsException {
        doNothing().when(dao).changeActivityStatus(anyString(), anyBoolean());
        service.changeActivityStatus("Test.Trainer", false);
        verify(dao, times(1)).changeActivityStatus("Test.Trainer", false);
    }

    @Test
    void testSelectTrainings() throws UserNotFoundException {
        when(dao.selectTrainings(anyString(), any(LocalDate.class), any(LocalDate.class), any(), any())).thenReturn(List.of(new Training(), new Training()));
        List<Training> trainings = service.selectTrainings("Test.Trainer", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), null, null);
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(2, trainings.size());
    }

    @Test
    void testSelect() throws UserNotFoundException {
        when(dao.select(anyString())).thenReturn(trainer);
        Trainer found = service.select("Test.Trainer");
        assertNotNull(found);
        assertEquals("Test.Trainer", found.getUsername());
    }

    @Test
    void testSelectAll() throws NoExpectedDataInDatabaseException {
        when(dao.selectAll()).thenReturn(List.of(trainer, new Trainer()));
        List<Trainer> trainers = service.selectAll();
        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
        assertEquals(2, trainers.size());
    }
}
