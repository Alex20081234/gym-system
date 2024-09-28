package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TrainingDaoImpl;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.service.TrainingServiceImpl;
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

class TrainingServiceImplTest {
    @Mock
    private TrainingDaoImpl dao;
    @InjectMocks
    private TrainingServiceImpl service;
    private final Trainee trainee = Trainee.builder()
            .username("Test.Trainee")
            .password("password")
            .firstName("Test")
            .lastName("User")
            .isActive(true)
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .address("Test address")
            .trainings(new ArrayList<>())
            .trainers(new HashSet<>())
            .build();
    private final Trainer trainer = Trainer.builder()
            .username("Test.Trainer")
            .password("password")
            .firstName("Test")
            .lastName("Trainer")
            .isActive(true)
            .specialization(null)
            .trainings(new ArrayList<>())
            .trainees(new HashSet<>())
            .build();
    private Training initial;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initial = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(null)
                .build();
    }

    @Test
    void testCreate() throws UserNotFoundException {
        doNothing().when(dao).create(any(Training.class));
        service.create(initial);
        verify(dao, times(1)).create(initial);
    }

    @Test
    void testSelect() throws TrainingNotFoundException {
        when(dao.select(1)).thenReturn(initial);
        Training actual = service.select(1);
        assertEquals(initial, actual);
    }

    @Test
    void testSelectAll() throws NoExpectedDataInDatabaseException {
        when(dao.selectAll()).thenReturn(List.of(initial, new Training()));
        List<Training> trainings = service.selectAll();
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(2, trainings.size());
    }
}