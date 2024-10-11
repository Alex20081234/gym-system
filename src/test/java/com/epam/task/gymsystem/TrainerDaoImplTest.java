package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.dao.TrainingDaoImpl;
import com.epam.task.gymsystem.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TrainerDaoImplTest {
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private EntityManager entityManager;
    private Trainer initial;

    @BeforeEach
    void setUp() {
        initial = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .specialization(entityManager.find(TrainingType.class, 1))
                .build();
    }

    void cleanUp(String username) {
        Trainer trainer = trainerDao.select(username);
        entityManager.remove(trainer);
    }

    void cleanTrainee(String username) {
        traineeDao.delete(username);
    }

    @Test
    void createShouldAddTrainerToDatabase() {
        trainerDao.create(initial);
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void changePasswordShouldMakeChangeToDatabase() {
        trainerDao.create(initial);
        trainerDao.changePassword("Test.Trainer", "newpassword");
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    @Test
    void updateShouldUpdateDatabase() {
        trainerDao.create(initial);
        Trainer updated = Trainer.builder()
                .username("Updated.Trainer")
                .password("password")
                .firstName("Updated")
                .lastName("Trainer")
                .isActive(true)
                .specialization(entityManager.find(TrainingType.class, 1))
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        trainerDao.update("Test.Trainer", updated);
        Trainer found = trainerDao.select("Updated.Trainer");
        cleanUp("Updated.Trainer");
        assertNotNull(found);
        assertEquals(updated, found);
    }

    @Test
    void changeActivityStatusShouldMakeChangeToDatabase() {
        trainerDao.create(initial);
        trainerDao.changeActivityStatus("Test.Trainer", false);
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertFalse(found.getIsActive());
    }

    @Test
    void selectTrainingsShouldReturnTrainings() {
        trainerDao.create(initial);
        Trainee trainee = Trainee.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .build();
        traineeDao.create(trainee);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(initial)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(LocalDate.now().minusDays(1))
                .toDate(LocalDate.now().plusDays(1))
                .partnerUsername("Test.Trainee")
                .trainingType(null)
                .build();
        List<Training> trainings;
        trainings = trainerDao.selectTrainings(trainerDao.select("Test.Trainer"), criteria);
        cleanUp("Test.Trainer");
        cleanTrainee("Test.Trainee");
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void selectTrainingsCriteriaShouldReturnTrainingsWhenCriteriaIsNull() {
        trainerDao.create(initial);
        Trainee trainee = Trainee.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .build();
        traineeDao.create(trainee);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(initial)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        List<Training> trainings;
        trainings = trainerDao.selectTrainings(trainerDao.select("Test.Trainer"), null);
        cleanUp("Test.Trainer");
        cleanTrainee("Test.Trainee");
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void selectShouldReturnTrainer() {
        trainerDao.create(initial);
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void selectShouldThrowUserNotFoundExceptionWhenTrainerIsNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> trainerDao.select("Non.Existent"));
        assertEquals("Trainer with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void selectAllShouldReturnAllTrainers() {
        trainerDao.create(initial);
        Trainer another = Trainer.builder()
                .username("Test.Trainer2")
                .password("password")
                .firstName("Test")
                .lastName("Trainer2")
                .isActive(true)
                .specialization(entityManager.find(TrainingType.class, 2))
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        trainerDao.create(another);
        List<Trainer> trainers = trainerDao.selectAll();
        cleanUp("Test.Trainer");
        cleanUp("Test.Trainer2");
        assertNotNull(trainers);
        trainers.forEach(trainer -> {
            if (trainer.getUsername().equals("Test.Trainer")) {
                assertEquals(initial, trainer);
            } else if (trainer.getUsername().equals("Test.Trainer2")) {
                assertEquals(another, trainer);
            }
        });
    }
}
