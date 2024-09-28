package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.dao.TrainingDaoImpl;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class TraineeDaoImplTest {
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
    private SessionFactory sessionFactory;
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
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", sessionFactory);
    }

    void cleanUp(String... usernames) {
        try {
            for (String username : usernames) {
                traineeDao.delete(username);
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void cleanTrainer(String username) throws UserNotFoundException {
        Trainer trainer = trainerDao.select(username);
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().remove(trainer);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Test
    void testCreate() throws UserNotFoundException {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        areSame(initial, found);
    }

    @Test
    void testCreateThrowsException() {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.create(initial));
    }

    @Test
    void testChangePassword() throws UserNotFoundException {
        traineeDao.create(initial);
        try {
            traineeDao.changePassword("Test.Trainee", "newpassword");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    @Test
    void testChangePasswordThrowsException() throws UserNotFoundException {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        doReturn(initial).when(traineeDao).select(anyString());
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.changePassword("Test.Trainee", "newpassword"));
    }

    @Test
    void testUpdate() throws UserNotFoundException {
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
        try {
            traineeDao.update("Test.Trainee", updated);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        Trainee found = traineeDao.select("Updated.Trainee");
        cleanUp("Updated.Trainee");
        assertNotNull(found);
        areSame(updated, found);
    }

    @Test
    void testUpdateThrowsException() throws UserNotFoundException {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        doReturn(initial).when(traineeDao).select(anyString());
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.update("Test.Trainee", initial));
    }

    @Test
    void testChangeActivityStatus() throws UserNotFoundException {
        traineeDao.create(initial);
        try {
            traineeDao.changeActivityStatus("Test.Trainee", false);
        } catch (UserNotFoundException | ActivityStatusAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        assertFalse(found.getIsActive());
    }

    @Test
    void testChangeActivityStatusThrowsException() throws UserNotFoundException {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        doReturn(initial).when(traineeDao).select(anyString());
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.changeActivityStatus("Test.Trainee", false));
    }

    @Test
    void testSelectTrainings() throws UserNotFoundException {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(null)
                .build();
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(initial)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(null)
                .build();
        try {
            trainingDao.create(training);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Training> trainings;
        try {
            trainings = traineeDao.selectTrainings("Test.Trainee", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), "Test.Trainer", null);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        cleanUp("Test.Trainee");
        cleanTrainer("Test.Trainer");
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void testSelectedTrainingsNoTrainingsSelected() throws UserNotFoundException {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(null)
                .build();
        trainerDao.create(trainer);
        Training training = Training.builder()
                .trainee(initial)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(null)
                .build();
        try {
            trainingDao.create(training);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Training> trainings;
        try {
            trainings = traineeDao.selectTrainings("Test.Trainee", LocalDate.now().plusDays(1), null, null, null);
            assertTrue(trainings.isEmpty());
            trainings = traineeDao.selectTrainings("Test.Trainee", null, LocalDate.now().minusDays(1), null, null);
            assertTrue(trainings.isEmpty());
            trainings = traineeDao.selectTrainings("Test.Trainee", LocalDate.now().plusDays(1), null, "Non.Existent", null);
            assertTrue(trainings.isEmpty());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        cleanUp("Test.Trainee");
        cleanTrainer("Test.Trainer");
    }

    @Test
    void testSelectNotAssignedTrainers() throws UserNotFoundException {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(null)
                .build();
        Trainer trainer2 = Trainer.builder()
                .username("Test.Trainer2")
                .password("password")
                .firstName("Test")
                .lastName("Trainer2")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(null)
                .build();
        trainerDao.create(trainer);
        trainerDao.create(trainer2);
        List<Trainer> trainers;
        try {
            trainers = traineeDao.selectNotAssignedTrainers("Test.Trainee");
        } catch (UserNotFoundException | NoExpectedDataInDatabaseException e) {
            throw new RuntimeException(e);
        }
        cleanUp("Test.Trainee");
        cleanTrainer("Test.Trainer");
        cleanTrainer("Test.Trainer2");
        assertNotNull(trainers);
        trainers.forEach(t -> {
            assertTrue(t.getUsername().equals("Test.Trainer") || t.getUsername().equals("Test.Trainer2"));
        });
    }

    @Test
    void testSelectNotAssignedTrainersNoTrainersSelected() {
        traineeDao.create(initial);
        assertThrows(NoExpectedDataInDatabaseException.class, () -> traineeDao.selectNotAssignedTrainers("Test.Trainee"));
        cleanUp("Test.Trainee");
    }

    @Test
    void testSelectNotAssignedTrainersThrowsException() throws UserNotFoundException {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        doReturn(initial).when(traineeDao).select(anyString());
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.selectNotAssignedTrainers("Test.Trainee"));
    }

    @Test
    void testUpdateTrainers() throws UserNotFoundException {
        traineeDao.create(initial);
        Trainer trainer = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .trainees(new HashSet<>())
                .trainings(new ArrayList<>())
                .specialization(null)
                .build();
        trainerDao.create(trainer);
        try {
            traineeDao.updateTrainers("Test.Trainee", Map.of("Test.Trainer", true));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        Trainee found = traineeDao.select("Test.Trainee");
        assertNotNull(found);
        found.getTrainers().forEach(t -> {
            assertEquals("Test.Trainer", t.getUsername());
        });
        try {
            traineeDao.updateTrainers("Test.Trainee", Map.of("Test.Trainer", false));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        found = traineeDao.select("Test.Trainee");
        assertTrue(found.getTrainers().isEmpty());
        cleanUp("Test.Trainee");
        cleanTrainer("Test.Trainer");
    }

    @Test
    void testUpdateTrainersNoSuchTrainer() {
        traineeDao.create(initial);
        assertThrows(UserNotFoundException.class, () -> traineeDao.updateTrainers("Test.Trainee", Map.of("Nonexistent.Trainer", true)));
        cleanUp("Test.Trainee");
    }

    @Test
    void testUpdateTrainersThrowsException() throws UserNotFoundException {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        doReturn(initial).when(traineeDao).select(anyString());
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.updateTrainers("Test.Trainee", Map.of("Test.Trainer", true)));
    }

    @Test
    void testDelete() {
        traineeDao.create(initial);
        try {
            traineeDao.delete("Test.Trainee");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertThrows(UserNotFoundException.class, () -> traineeDao.select("Test.Trainee"));
    }

    @Test
    void testDeleteThrowsException() throws UserNotFoundException {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        doReturn(initial).when(traineeDao).select(anyString());
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.delete("Test.Trainee"));
    }

    @Test
    void testSelect() throws UserNotFoundException {
        traineeDao.create(initial);
        Trainee found = traineeDao.select("Test.Trainee");
        cleanUp("Test.Trainee");
        assertNotNull(found);
        areSame(initial, found);
    }

    @Test
    void testSelectThrowsException() {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.select("Test.Trainee"));
    }

    @Test
    void testSelectAll() throws NoExpectedDataInDatabaseException {
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
                areSame(initial, trainee);
            } else if (trainee.getUsername().equals("Test.Trainee2")) {
                areSame(another, trainee);
            }
        });
    }

    @Test
    void testSelectAllNoTraineesSelected() {
        assertThrows(NoExpectedDataInDatabaseException.class, () -> traineeDao.selectAll());
    }

    @Test
    void testSelectAllThrowsException() {
        traineeDao = Mockito.spy(traineeDao);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        when(mockSessionFactory.getCurrentSession()).thenThrow(HibernateException.class);
        ReflectionTestUtils.setField(traineeDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> traineeDao.selectAll());
    }

    private void areSame(Trainee expected, Trainee actual) {
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getIsActive(), actual.getIsActive());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getAddress(), actual.getAddress());
        if (expected.getTrainings() == null) {
            assertNull(actual.getTrainings());
        } else {
            assertEquals(expected.getTrainings().stream().toList(), actual.getTrainings().stream().toList());
        }
        if (expected.getTrainers() == null) {
            assertNull(actual.getTrainers());
        } else {
            assertEquals(expected.getTrainers().stream().toList(), actual.getTrainers().stream().toList());
        }
    }

    @Test
    void testCreateWithNullUsername() {
        initial.setUsername(null);
        assertThrows(IllegalArgumentException.class, () -> traineeDao.create(initial));
    }

    @Test
    void testChangePasswordWithNonexistentUser() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.changePassword("Nonexistent.User", "newpassword"));
    }

    @Test
    void testUpdateWithNonexistentUser() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.update("Nonexistent.User", initial));
    }

    @Test
    void testChangeActivityStatusWithInvalidUsername() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.changeActivityStatus("InvalidUsername", false));
    }

    @Test
    void testChangeActivityStatusWithExistingStatus() {
        traineeDao.create(initial);
        assertThrows(ActivityStatusAlreadyExistsException.class, () -> traineeDao.changeActivityStatus("Test.Trainee", true));
        cleanUp("Test.Trainee");
    }

    @Test
    void testSelectTrainingsWithInvalidTrainee() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.selectTrainings("InvalidTrainee", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), null, null));
    }

    @Test
    void testUpdateTrainersWithInvalidTrainee() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.updateTrainers("InvalidTrainee", Map.of("Test.Trainer", true)));
    }

    @Test
    void testUpdateWithNullUsername() {
        initial.setUsername(null);
        assertThrows(IllegalArgumentException.class, () -> traineeDao.update("Test.Trainee", initial));
    }

    @Test
    void testDeleteWithNonexistentUser() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.delete("Nonexistent.Trainee"));
    }

    @Test
    void testSelectWithInvalidUsername() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.select("InvalidUsername"));
    }

    @Test
    void testSelectNotAssignedTrainersWithInvalidTrainee() {
        assertThrows(UserNotFoundException.class, () -> traineeDao.selectNotAssignedTrainers("InvalidTrainee"));
    }

    @Test
    void testUpdatePasswordWithInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () -> traineeDao.changePassword("Nonexistent.Trainee", ""));
    }
}