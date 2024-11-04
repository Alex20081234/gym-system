package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TrainingDaoImplIT {
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
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

    void cleanUpTrainer(Trainer current) {
        entityManager.remove(current);
    }

    @Test
    void createShouldAddTrainingToDatabase() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Training found = trainingDao.select(initial.getId()).orElse(null);
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainer);
        assertEquals(initial, found);
    }

    @Test
    void selectShouldReturnTraining() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Training found = trainingDao.select(initial.getId()).orElse(null);
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainer);
        assertEquals(initial, found);
    }

    @Test
    void selectShouldReturnEmptyWhenNoSuchTraining() {
        assertEquals(Optional.empty(), trainingDao.select(0));
    }

    @Test
    void selectAllShouldReturnAllTrainings() {
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
            if (training.getId().equals(initial.getId())) {
                assertEquals(initial, training);
            } else if (training.getId().equals(another.getId())) {
                assertEquals(another, training);
            }
        });
    }

    @Test
    void selectTrainingsByCriteriaShouldReturnApplicableTrainings() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(LocalDate.now().minusDays(1))
                .toDate(LocalDate.now().plusDays(1))
                .partnerUsername("Test.Trainer")
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        List<Training> trainings = trainingDao.selectTrainings("Test.Trainee", criteria);
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainerDao.select("Test.Trainer").orElse(null));
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void selectTrainingsByCriteriaShouldReturnTrainingsWhenCriteriaIsNull() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        List<Training> trainings = trainingDao.selectTrainings("Test.Trainee", null);
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainerDao.select("Test.Trainer").orElse(null));
    }

    @Test
    void selectTrainingsShouldReturnEmptyListWhenNoSuitableTrainings() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(LocalDate.now().plusDays(1))
                .toDate(LocalDate.now().minusDays(1))
                .partnerUsername("Non.Existent")
                .trainingType(null)
                .build();
        List<Training> trainings = trainingDao.selectTrainings("Test.Trainer", criteria);
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainerDao.select("Test.Trainer").orElse(null));
        assertNotNull(trainings);
        assertEquals(0, trainings.size());
    }

    @Test
    void selectTrainingsShouldReturnTrainingsWhenCriteriaParamsNull() {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(null)
                .toDate(null)
                .partnerUsername(null)
                .trainingType(null)
                .build();
        List<Training> trainings = trainingDao.selectTrainings("Test.Trainer", criteria);
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer(trainerDao.select("Test.Trainer").orElse(null));
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void selectTrainingsShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> trainingDao.selectTrainings("Non.Existent", null));
        assertEquals("User with username Non.Existent was not found", e.getMessage());
    }
}
