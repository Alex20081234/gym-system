package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.configuration.GymSystemConfiguration;
import com.epam.gymsystem.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GymSystemConfiguration.class})
@WebAppConfiguration
@Transactional
class TrainerDaoImplTest {
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
    private TraineeDaoImpl traineeDao;
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
        Trainer trainer = trainerDao.select(username).orElse(null);
        entityManager.remove(trainer);
    }

    void cleanTrainee(String username) {
        traineeDao.delete(username);
    }

    @Test
    void createShouldAddTrainerToDatabase() {
        trainerDao.create(initial);
        Trainer found = trainerDao.select("Test.Trainer").orElse(null);
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void changePasswordShouldMakeChangeToDatabase() {
        trainerDao.create(initial);
        trainerDao.changePassword("Test.Trainer", "newpassword");
        Trainer found = trainerDao.select("Test.Trainer").orElse(null);
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    @Test
    void changePasswordShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> trainerDao.changePassword("Non.Existent", "password"));
        assertEquals("Trainer with username Non.Existent was not found", e.getMessage());
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
        Trainer found = trainerDao.select("Updated.Trainer").orElse(null);
        cleanUp("Updated.Trainer");
        assertNotNull(found);
        assertEquals(updated, found);
    }

    @Test
    void changeActivityStatusShouldMakeChangeToDatabase() {
        trainerDao.create(initial);
        trainerDao.changeActivityStatus("Test.Trainer", false);
        Trainer found = trainerDao.select("Test.Trainer").orElse(null);
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertFalse(found.getIsActive());
    }

    @Test
    void changeActivityStatusShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> trainerDao.changeActivityStatus("Non.Existent", true));
        assertEquals("Trainer with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void selectShouldReturnTrainer() {
        trainerDao.create(initial);
        Trainer found = trainerDao.select("Test.Trainer").orElse(null);
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void selectShouldReturnEmptyWhenTrainerIsNonExistent() {
        assertEquals(Optional.empty(), trainerDao.select("Non.Existent"));
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

    @Test
    void selectUsernamesShouldReturnAllUsernames() {
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
        List<String> usernames = trainerDao.selectUsernames();
        cleanUp("Test.Trainer");
        cleanUp("Test.Trainer2");
        assertNotNull(usernames);
        assertTrue(usernames.contains("Test.Trainer"));
        assertTrue(usernames.contains("Test.Trainer2"));
    }
}
