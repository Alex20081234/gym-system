package com.epam.gymsystem.service;

import com.epam.gymsystem.dao.TrainingDaoImpl;
import com.epam.gymsystem.dao.TrainingTypeDaoImpl;
import com.epam.gymsystem.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {
    @Mock
    private TrainingDaoImpl dao;
    @Mock
    private TrainingTypeDaoImpl typeDao;
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
                .trainingType(new TrainingType())
                .build();
    }

    @Test
    void createShouldTryToAddTrainingToDatabase() {
        doNothing().when(dao).create(any(Training.class));
        service.create(initial);
        verify(dao, times(1)).create(initial);
    }

    @Test
    void createShouldThrowExceptionWhenInvalidTraining() {
        String notValid = "Training is not valid";
        RuntimeException e = assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertEquals(notValid, e.getMessage());
        initial.setTrainee(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
        setUp();
        initial.setTrainer(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
        setUp();
        initial.setTrainingName(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
        setUp();
        initial.setTrainingName("");
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
        setUp();
        initial.setTrainingType(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
        setUp();
        initial.setTrainingDate(null);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
        setUp();
        initial.setDuration(0);
        e = assertThrows(IllegalArgumentException.class, () -> service.create(initial));
        assertEquals(notValid, e.getMessage());
    }

    @Test
    void selectShouldTryToReturnTraining() {
        when(dao.select(1)).thenReturn(Optional.of(initial));
        Training actual = service.select(1).orElse(null);
        assertEquals(initial, actual);
    }

    @Test
    void selectAllShouldTryToReturnAllTrainings() {
        when(dao.selectAll()).thenReturn(List.of(initial, new Training()));
        List<Training> trainings = service.selectAll();
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(2, trainings.size());
    }

    @Test
    void selectTypeShouldTryToReturnTrainingType() {
        TrainingType type = TrainingType.builder()
                        .name("Yoga")
                                .id(1)
                                        .build();
        when(typeDao.selectType("Yoga")).thenReturn(Optional.of(type));
        TrainingType found = service.selectType("Yoga").orElse(null);
        assertEquals(type, found);
        verify(typeDao, times(1)).selectType("Yoga");
    }

    @Test
    void selectAllTypesShouldTryToReturnAllTrainingTypes() {
        when(typeDao.selectAllTypes()).thenReturn(List.of(new TrainingType(), new TrainingType()));
        List<TrainingType> types = service.selectAllTypes();
        assertNotNull(types);
        assertEquals(2, types.size());
        verify(typeDao, times(1)).selectAllTypes();
    }

    @Test
    void selectTrainingsShouldTrySelectApplicableTrainings() {
        TrainingCriteria criteria = TrainingCriteria.builder().build();
        when(dao.selectTrainings(anyString(), any())).thenReturn(List.of(new Training(), new Training()));
        List<Training> trainings = service.selectTrainings("Test.User", criteria);
        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        verify(dao, times(1)).selectTrainings("Test.User", criteria);
    }
}
