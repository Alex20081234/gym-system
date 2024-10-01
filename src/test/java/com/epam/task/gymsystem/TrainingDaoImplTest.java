package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.dao.TrainingDaoImpl;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TrainingDaoImplTest {
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    @PersistenceContext
    private EntityManager entityManager;

    private final Trainee trainee = Trainee.builder()
            .username("Test.Trainee")
            .password("password")
            .firstName("Test")
            .lastName("Trainee")
            .isActive(true)
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .address("Test address")
            .build();
    private final Trainer trainer = Trainer.builder()
            .username("Test.Trainer")
            .password("password")
            .firstName("Test")
            .lastName("Trainer")
            .isActive(true)
            .build();
    private Training initial;

    @BeforeEach
    void setUp() {
        trainer.setSpecialization(entityManager.find(TrainingType.class, 1));
        initial = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
    }

    void cleanUpTrainee(String current) {
        traineeDao.delete(current);
    }

    @Transactional
    void cleanUpTrainer(Trainer current) {
        entityManager.remove(current);
    }

    @Test
    void create() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Training found = trainingDao.select(initial.getId());
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainer);
        assertEquals(initial, found);
    }

    @Test
    void select() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Training found = trainingDao.select(initial.getId());
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainer);
        assertEquals(initial, found);
    }

    @Test
    void selectThrowsTrainingNotFoundException() {
        assertThrows(TrainingNotFoundException.class, () -> trainingDao.select(0));
    }

    @Test
    void selectAll() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Trainee anotherTrainee = Trainee.builder()
                .username("Another.Trainee")
                .password("password")
                .firstName("Another")
                .lastName("Trainee")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Another address")
                .build();
        Trainer anotherTrainer = Trainer.builder()
                .username("Another.Trainer")
                .password("password")
                .firstName("Another")
                .lastName("Trainer")
                .isActive(true)
                .specialization(entityManager.find(TrainingType.class, 1))
                .build();
        Training another = Training.builder()
                .trainee(anotherTrainee)
                .trainer(anotherTrainer)
                .trainingDate(LocalDate.now())
                .trainingName("Another training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        traineeDao.create(anotherTrainee);
        trainerDao.create(anotherTrainer);
        trainingDao.create(another);
        List<Training> trainings = trainingDao.selectAll();
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainer);
        cleanUpTrainee("Another.Trainee");
        cleanUpTrainer(anotherTrainer);
        assertFalse(trainings.isEmpty());
        trainings.forEach(training -> {
            if (training.getId() == initial.getId()) {
                assertEquals(initial, training);
            } else if (training.getId() == another.getId()) {
                assertEquals(another, training);
            }
        });
    }
}
