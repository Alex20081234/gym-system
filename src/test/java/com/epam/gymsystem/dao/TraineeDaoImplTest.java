package com.epam.gymsystem.dao;

import com.epam.gymsystem.configuration.GymSystemConfiguration;
import com.epam.gymsystem.domain.*;
import com.epam.gymsystem.common.UserNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GymSystemConfiguration.class})
@WebAppConfiguration
@Transactional
class TraineeDaoImplTest {
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
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

    void cleanTrainer(Trainer current) {
        entityManager.remove(current);
    }

    @Test
    void createShouldAddTraineeToDatabase() {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void changePasswordShouldMakeChangeToDatabase() {
        traineeDao.create(initial);
        traineeDao.changePassword("Test.Trainee", "newpassword");
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    @Test
    void updateShouldUpdateDatabase() {
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
        traineeDao.update("Test.Trainee", updated);
        Trainee found = traineeDao.select("Updated.Trainee");
        cleanUp("Updated.Trainee");
        assertNotNull(found);
        assertEquals(updated, found);
    }

    @Test
    void changeActivityStatusShouldMakeChangeToDatabase() {
        traineeDao.create(initial);
        traineeDao.changeActivityStatus("Test.Trainee", false);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertFalse(found.getIsActive());
    }

    @Test
    void selectNotAssignedTrainersShouldReturnTrainers() {
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
    void selectNotAssignedTrainersShouldReturnNoTrainersWhenNoTrainersExist() {
        traineeDao.create(initial);
        assertEquals(Collections.emptyList(), traineeDao.selectNotAssignedTrainers(traineeDao.select("Test.Trainee")));
        cleanUp("Test.Trainee");
    }

    @Test
    void updateTrainersShouldUpdateDatabase(){
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
    void updateTrainersShouldThrowUserNotFoundExceptionWhenTrainerIsNonExistent() {
        traineeDao.create(initial);
        Trainee trainee = traineeDao.select("Test.Trainee");
        Map<String, Boolean> map = Map.of("Nonexistent.Trainer", true);
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> traineeDao.updateTrainers(trainee, map));
        assertEquals("Trainer with username Nonexistent.Trainer was not found", e.getMessage());
        cleanUp("Test.Trainee");
    }

    @Test
    void deleteShouldDeleteTraineeFromDatabase() {
        traineeDao.create(initial);
        traineeDao.delete("Test.Trainee");
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> traineeDao.select("Test.Trainee"));
        assertEquals("Trainee with username Test.Trainee was not found", e.getMessage());
    }

    @Test
    void selectShouldReturnTrainee() {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void selectShouldThrowUserNotFoundExceptionWhenTraineeIsNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> traineeDao.select("Nonexistent.Trainee"));
        assertEquals("Trainee with username Nonexistent.Trainee was not found", e.getMessage());
    }

    @Test
    void selectAllShouldReturnAllTrainees() {
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

    @Test
    void selectUsernamesShouldReturnAllUsernames() {
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
        List<String> usernames = traineeDao.selectUsernames();
        cleanUp("Test.Trainee", "Test.Trainee2");
        assertNotNull(usernames);
        assertTrue(usernames.contains("Test.Trainee"));
        assertTrue(usernames.contains("Test.Trainee2"));
    }
}
