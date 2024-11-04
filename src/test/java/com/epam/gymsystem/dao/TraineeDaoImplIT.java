package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.*;
import com.epam.gymsystem.common.UserNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TraineeDaoImplIT {
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
    private TrainingDaoImpl trainingDao;
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
        Trainee found = traineeDao.select("Test.Trainee").orElse(null);
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void changePasswordShouldMakeChangeToDatabase() {
        traineeDao.create(initial);
        traineeDao.changePassword("Test.Trainee", "newpassword");
        Trainee found = traineeDao.select("Test.Trainee").orElse(null);
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
        Trainee found = traineeDao.select("Updated.Trainee").orElse(null);
        cleanUp("Updated.Trainee");
        assertNotNull(found);
        assertEquals(updated, found);
    }

    @Test
    void changeActivityStatusShouldMakeChangeToDatabase() {
        traineeDao.create(initial);
        traineeDao.changeActivityStatus("Test.Trainee", false);
        Trainee found = traineeDao.select("Test.Trainee").orElse(null);
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
        Training training = Training.builder()
                .trainee(initial)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(entityManager.find(TrainingType.class, 1))
                .build();
        trainingDao.create(training);
        trainerDao.create(trainer2);
        List<Trainer> trainers = traineeDao.selectNotAssignedTrainers(traineeDao.select("Test.Trainee").orElse(null));
        cleanUp("Test.Trainee");
        cleanTrainer(trainerDao.select("Test.Trainer").orElse(null));
        cleanTrainer(trainerDao.select("Test.Trainer2").orElse(null));
        assertNotNull(trainers);
        assertEquals("Test.Trainer2", trainers.get(0).getUsername());
    }

    @Test
    void selectNotAssignedTrainersShouldReturnNoTrainersWhenNoTrainersExist() {
        traineeDao.create(initial);
        assertEquals(Collections.emptyList(), traineeDao.selectNotAssignedTrainers(traineeDao.select("Test.Trainee").orElse(null)));
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
        traineeDao.updateTrainers(traineeDao.select("Test.Trainee").orElse(null), Map.of("Test.Trainer", true));
        Trainee found = traineeDao.select("Test.Trainee").orElse(null);
        assertNotNull(found);
        found.getTrainers().forEach(t -> {
            assertEquals("Test.Trainer", t.getUsername());
        });
        traineeDao.updateTrainers(traineeDao.select("Test.Trainee").orElse(null), Map.of("Test.Trainer", false));
        found = traineeDao.select("Test.Trainee").orElse(null);
        assertTrue(found.getTrainers().isEmpty());
        cleanUp("Test.Trainee");
        cleanTrainer(trainerDao.select("Test.Trainer").orElse(null));
    }

    @Test
    void updateTrainersShouldThrowUserNotFoundExceptionWhenTrainerIsNonExistent() {
        traineeDao.create(initial);
        Trainee trainee = traineeDao.select("Test.Trainee").orElse(null);
        Map<String, Boolean> map = Map.of("Nonexistent.Trainer", true);
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> traineeDao.updateTrainers(trainee, map));
        assertEquals("Trainer with username Nonexistent.Trainer was not found", e.getMessage());
        cleanUp("Test.Trainee");
    }

    @Test
    void deleteShouldDeleteTraineeFromDatabase() {
        traineeDao.create(initial);
        traineeDao.delete("Test.Trainee");
        assertEquals(Optional.empty(), traineeDao.select("Test.Trainee"));
    }

    @Test
    void deleteShouldThrowUserNotFoundExceptionWhenUserNonExistent() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> traineeDao.delete("Non.Existent"));
        assertEquals("Trainee with username Non.Existent was not found", e.getMessage());
    }

    @Test
    void selectShouldReturnTrainee() {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee").orElse(null);
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals(initial, found);
    }

    @Test
    void selectShouldReturnEmptyWhenTraineeIsNonExistent() {
        assertEquals(Optional.empty(), traineeDao.select("Nonexistent.Trainee"));
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

    @Test
    void loadDependenciesShouldRefreshObject() {
        traineeDao.create(initial);
        Integer traineeId = initial.getId();
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
        Integer trainerId = trainer.getId();
        String sql = "insert into trainee_trainer (trainee_id, trainer_id) values (?, ?)";
        entityManager.createNativeQuery(sql)
                .setParameter(1, traineeId)
                .setParameter(2, trainerId)
                .executeUpdate();
        assertNull(initial.getTrainers());
        traineeDao.loadDependencies(initial);
        assertFalse(initial.getTrainers().isEmpty());
        assertTrue(initial.getTrainers().contains(trainer));
        cleanUp("Test.Trainee");
        cleanTrainer(trainer);
    }
}
