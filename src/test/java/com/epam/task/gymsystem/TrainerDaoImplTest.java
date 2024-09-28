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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TrainerDaoImplTest {
    @Autowired
    private TrainerDaoImpl trainerDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainingDaoImpl trainingDao;
    private Trainer initial;

    @BeforeEach
    void setUp() {
        initial = Trainer.builder()
                .username("Test.Trainer")
                .password("password")
                .firstName("Test")
                .lastName("Trainer")
                .isActive(true)
                .specialization(null)
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
    }

    void cleanUp(String username) throws UserNotFoundException {
        Trainer trainer = trainerDao.select(username);
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().remove(trainer);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    void cleanTrainee(String username) throws UserNotFoundException {
        traineeDao.delete(username);
    }

    @Test
    void testCreate() throws UserNotFoundException {
        trainerDao.create(initial);
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        areSame(initial, found);
    }

    @Test
    void testCreateWithNullUsername() {
        initial.setUsername(null);
        assertThrows(IllegalArgumentException.class, () -> trainerDao.create(initial));
    }

    @Test
    void testCreateThrowsException() throws UserNotFoundException {
        trainerDao.create(initial);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainerDao.create(initial));
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
        cleanUp("Test.Trainer");
    }

    @Test
    void testChangePassword() throws UserNotFoundException {
        trainerDao.create(initial);
        trainerDao.changePassword("Test.Trainer", "newpassword");
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    @Test
    void testChangePasswordWithNullNewPassword() {
        assertThrows(IllegalArgumentException.class, () -> trainerDao.changePassword("Test.Trainer", null));
    }

    @Test
    void testChangePasswordThrowsException() throws UserNotFoundException {
        trainerDao.create(initial);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainerDao.changePassword("Test.Trainer", "newpassword"));
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
        cleanUp("Test.Trainer");
    }

    @Test
    void testUpdate() throws UserNotFoundException {
        trainerDao.create(initial);
        Trainer updated = Trainer.builder()
                .username("Updated.Trainer")
                .password("password")
                .firstName("Updated")
                .lastName("Trainer")
                .isActive(true)
                .specialization(null)
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        trainerDao.update("Test.Trainer", updated);
        Trainer found = trainerDao.select("Updated.Trainer");
        cleanUp("Updated.Trainer");
        assertNotNull(found);
        areSame(updated, found);
    }

    @Test
    void testUpdateWithNullUsername() {
        initial.setUsername(null);
        assertThrows(IllegalArgumentException.class, () -> trainerDao.update("Test.Trainer", initial));
    }

    @Test
    void testUpdateThrowsException() throws UserNotFoundException {
        trainerDao.create(initial);
        Trainer updated = Trainer.builder()
                .username("Updated.Trainer")
                .password("password")
                .firstName("Updated")
                .lastName("Trainer")
                .isActive(true)
                .specialization(null)
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainerDao.update("Test.Trainer", updated));
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
        cleanUp("Test.Trainer");
    }

    @Test
    void testChangeActivityStatus() throws UserNotFoundException, ActivityStatusAlreadyExistsException {
        trainerDao.create(initial);
        trainerDao.changeActivityStatus("Test.Trainer", false);
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        assertFalse(found.getIsActive());
    }

    @Test
    void testChangeActivityStatusThrowsException() throws UserNotFoundException {
        trainerDao.create(initial);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainerDao.changeActivityStatus("Test.Trainer", false));
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
        cleanUp("Test.Trainer");
    }

    @Test
    void testChangeActivityStatusWithAlreadyExistentStatus() throws UserNotFoundException {
        trainerDao.create(initial);
        assertThrows(ActivityStatusAlreadyExistsException.class, () -> trainerDao.changeActivityStatus("Test.Trainer", true));
        cleanUp("Test.Trainer");
    }

    @Test
    void testSelectTrainings() throws UserNotFoundException {
        trainerDao.create(initial);
        Trainee trainee = Trainee.builder()
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
        traineeDao.create(trainee);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(initial)
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
            trainings = trainerDao.selectTrainings("Test.Trainer", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), "Test.Trainee", null);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        cleanTrainee("Test.Trainee");
        cleanUp("Test.Trainer");
        assertNotNull(trainings);
        assertEquals(1, trainings.size());
    }

    @Test
    void testSelectedTrainingsNoTrainingsSelected() throws UserNotFoundException {
        trainerDao.create(initial);
        Trainee trainee = Trainee.builder()
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
        traineeDao.create(trainee);
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(initial)
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
            trainings = trainerDao.selectTrainings("Test.Trainer", LocalDate.now().plusDays(1), null, null, null);
            assertTrue(trainings.isEmpty());
            trainings = trainerDao.selectTrainings("Test.Trainer", null, LocalDate.now().minusDays(1), null, null);
            assertTrue(trainings.isEmpty());
            trainings = trainerDao.selectTrainings("Test.Trainer", LocalDate.now().plusDays(1), null, "Non.Existent", null);
            assertTrue(trainings.isEmpty());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        cleanTrainee("Test.Trainee");
        cleanUp("Test.Trainer");
    }

    @Test
    void testSelect() throws UserNotFoundException {
        trainerDao.create(initial);
        Trainer found = trainerDao.select("Test.Trainer");
        cleanUp("Test.Trainer");
        assertNotNull(found);
        areSame(initial, found);
    }

    @Test
    void testSelectNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> trainerDao.select("Non.Existent"));
    }

    @Test
    void testSelectThrowsException() throws UserNotFoundException {
        trainerDao.create(initial);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainerDao.select("Test.Trainer"));
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
        cleanUp("Test.Trainer");
    }

    @Test
    void testSelectAll() throws NoExpectedDataInDatabaseException, UserNotFoundException {
        trainerDao.create(initial);
        Trainer another = Trainer.builder()
                .username("Test.Trainer2")
                .password("password")
                .firstName("Test")
                .lastName("Trainer2")
                .isActive(true)
                .specialization(null)
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
                areSame(initial, trainer);
            } else if (trainer.getUsername().equals("Test.Trainer2")) {
                areSame(another, trainer);
            }
        });
    }

    @Test
    void testSelectAllThrowsException() throws UserNotFoundException {
        trainerDao.create(initial);
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainerDao.selectAll());
        ReflectionTestUtils.setField(trainerDao, "sessionFactory", sessionFactory);
        cleanUp("Test.Trainer");
    }

    @Test
    void testSelectAllThrowsNoExpectedDataInDatabaseException() {
        assertThrows(NoExpectedDataInDatabaseException.class, () -> trainerDao.selectAll());
    }

    private void areSame(Trainer expected, Trainer actual) {
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getIsActive(), actual.getIsActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        if (expected.getTrainings() == null) {
            assertNull(actual.getTrainings());
        } else {
            assertEquals(expected.getTrainings().stream().toList(), actual.getTrainings().stream().toList());
        }
        if (expected.getTrainees() == null) {
            assertNull(actual.getTrainees());
        } else {
            assertEquals(expected.getTrainees().stream().toList(), actual.getTrainees().stream().toList());
        }
    }
}
