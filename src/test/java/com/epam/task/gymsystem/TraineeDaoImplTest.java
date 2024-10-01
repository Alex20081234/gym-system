package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.dao.TrainingDaoImpl;
import com.epam.task.gymsystem.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TraineeDaoImplTest {
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    @PersistenceContext
    private EntityManager entityManager;
    private Trainee initial;

    @BeforeEach
    void setUp() {
        initial = Trainee.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .build();
    }

    void cleanUp(String... usernames) {
        for (String username : usernames) {
            traineeDao.delete(username);
        }
    }

    @Transactional
    void cleanTrainer(Trainer current) {
        entityManager.remove(current);
    }

    @Test
    void create() {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void changePassword() {
        traineeDao.create(initial);
        traineeDao.changePassword(traineeDao.select("Test.Trainee"), "newpassword");
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    @Test
    void update() {
        traineeDao.create(initial);
        Trainee updated = Trainee.builder()
                .username("Updated.Trainee")
                .password("password")
                .firstName("Updated")
                .lastName("Trainee")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Updated address")
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
        traineeDao.update(traineeDao.select("Test.Trainee"), updated);
        Trainee found = traineeDao.select("Updated.Trainee");
        cleanUp("Updated.Trainee");
        assertNotNull(found);
        assertEquals(updated, found);
    }

    @Test
    void changeActivityStatus() {
        traineeDao.create(initial);
        traineeDao.changeActivityStatus(traineeDao.select("Test.Trainee"), false);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertFalse(found.getIsActive());
    }

    @Test
    void selectTrainings() {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .specialization(entityManager.find(TrainingType.class, 1))
                .build();
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(initial)
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
                .trainingType(null)
                .build();
        List<Training> trainings = traineeDao.selectTrainings(traineeDao.select("Test.Trainee"), criteria);
        cleanUp("Test.Trainee");
        cleanTrainer(trainerDao.select("Test.Trainer"));
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void selectTrainingsWithNullCriteria() {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .specialization(entityManager.find(TrainingType.class, 1))
                .build();
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(initial)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        List<Training> trainings = traineeDao.selectTrainings(traineeDao.select("Test.Trainee"), null);
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
        cleanUp("Test.Trainee");
        cleanTrainer(trainerDao.select("Test.Trainer"));
    }

    @Test
    void selectNotAssignedTrainers() {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(entityManager.find(TrainingType.class, 3))
                .build();
        Trainer trainer2 = Trainer.builder()
                .username("Test.Trainer2")
                .password("password")
                .firstName("Test")
                .lastName("Trainer2")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(entityManager.find(TrainingType.class, 4))
                .build();
        trainerDao.create(trainer);
        trainerDao.create(trainer2);
        List<Trainer> trainers = traineeDao.selectNotAssignedTrainers(traineeDao.select("Test.Trainee"));
        cleanUp("Test.Trainee");
        cleanTrainer(trainerDao.select("Test.Trainer"));
        cleanTrainer(trainerDao.select("Test.Trainer2"));
        assertNotNull(trainers);
        trainers.forEach(t -> {
            assertTrue(t.getUsername().equals("Test.Trainer") || t.getUsername().equals("Test.Trainer2"));
        });
    }

    @Test
    void selectNotAssignedTrainersNoTrainersSelected() {
        traineeDao.create(initial);
        assertEquals(Collections.emptyList(), traineeDao.selectNotAssignedTrainers(traineeDao.select("Test.Trainee")));
        cleanUp("Test.Trainee");
    }

    @Test
    void updateTrainers(){
        traineeDao.create(initial);
        TrainingType trainingType = entityManager.find(TrainingType.class, 1);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(trainingType)
                .build();
        trainerDao.create(trainer);
        traineeDao.updateTrainers(traineeDao.select("Test.Trainee"), Map.of("Test.Trainer", true));
        Trainee found = traineeDao.select("Test.Trainee");
        assertNotNull(found);
        found.getTrainers().forEach(t -> {
            assertEquals("Test.Trainer", t.getUsername());
        });
        traineeDao.updateTrainers(traineeDao.select("Test.Trainee"), Map.of("Test.Trainer", false));
        found = traineeDao.select("Test.Trainee");
        assertTrue(found.getTrainers().isEmpty());
        cleanUp("Test.Trainee");
        cleanTrainer(trainerDao.select("Test.Trainer"));
    }

    @Test
    void updateTrainersNoSuchTrainer() {
        traineeDao.create(initial);
        assertThrows(UserNotFoundException.class, () -> traineeDao.updateTrainers(traineeDao.select("Test.Trainee"), Map.of("Nonexistent.Trainer", true)));
        cleanUp("Test.Trainee");
    }

    @Test
    void delete() {
        traineeDao.create(initial);
        traineeDao.delete("Test.Trainee");
        assertThrows(UserNotFoundException.class, () -> traineeDao.select("Test.Trainee"));
    }

    @Test
    void select() {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void selectThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.select("Nonexistent.Trainee"));
    }

    @Test
    void selectAll() {
        traineeDao.create(initial);
        Trainee another = Trainee.builder()
                .username("Test.Trainee2")
                .password("password")
                .firstName("Test")
                .lastName("Trainee2")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
        traineeDao.create(another);
        List<Trainee> trainees = traineeDao.selectAll();
        cleanUp("Test.Trainee", "Test.Trainee2");
        assertNotNull(trainees);
        trainees.forEach(trainee -> {
            if (trainee.getUsername().equals("Test.Trainee")) {
                assertEquals(initial, trainee);
            } else if (trainee.getUsername().equals("Test.Trainee2")) {
                assertEquals(another, trainee);
            }
        });
    }
}