package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
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
class TrainingDaoImplTest {
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private TrainerDaoImpl trainerDao;
    private final Trainee trainee = Trainee.builder()
            .username("Test.Trainee")
            .password("password")
            .firstName("Test")
            .lastName("Trainee")
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
        initial = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingName("Test training")
                .duration(60)
                .trainingType(null)
                .build();
        ReflectionTestUtils.setField(trainingDao, "sessionFactory", sessionFactory);
    }

    void cleanUp(int id) throws TrainingNotFoundException {
        Training current = trainingDao.select(id);
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().remove(current);
        sessionFactory.getCurrentSession().getTransaction().commit();

    }

    void cleanUpTrainee(String username) throws UserNotFoundException {
        Trainee current = traineeDao.select(username);
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().remove(current);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    void cleanUpTrainer(String username) throws UserNotFoundException {
        Trainer current = trainerDao.select(username);
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().remove(current);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Test
    void testCreate() throws UserNotFoundException, TrainingNotFoundException {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Training found = trainingDao.select(initial.getId());
        cleanUp(initial.getId());
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer("Test.Trainer");
        assertEquals(initial, found);
    }

    @Test
    void testCreateWithNonExistingTrainee() throws UserNotFoundException {
        trainerDao.create(trainer);
        assertThrows(UserNotFoundException.class, () -> trainingDao.create(initial));
        cleanUpTrainer("Test.Trainer");
    }

    @Test
    void testCreateWithNonExistingTrainer() throws UserNotFoundException {
        traineeDao.create(trainee);
        assertThrows(UserNotFoundException.class, () -> trainingDao.create(initial));
        cleanUpTrainee("Test.Trainee");
    }

    @Test
    void testCreateThrowsException() {
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainingDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainingDao.create(initial));
    }

    @Test
    void testSelect() throws UserNotFoundException, TrainingNotFoundException {
        traineeDao.create(trainee);
        trainerDao.create(trainer);
        trainingDao.create(initial);
        Training found = trainingDao.select(initial.getId());
        cleanUp(initial.getId());
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainer("Test.Trainer");
        assertEquals(initial, found);
    }

    @Test
    void testSelectThrowsException() {
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainingDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainingDao.select(1));
    }

    @Test
    void testSelectThrowsTrainingNotFoundException() {
        assertThrows(TrainingNotFoundException.class, () -> trainingDao.select(1));
    }

    @Test
    void testSelectAll() throws UserNotFoundException, TrainingNotFoundException, NoExpectedDataInDatabaseException {
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
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
        Trainer anotherTrainer = Trainer.builder()
                .username("Another.Trainer")
                .password("password")
                .firstName("Another")
                .lastName("Trainer")
                .isActive(true)
                .specialization(null)
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        Training another = Training.builder()
                .trainee(anotherTrainee)
                .trainer(anotherTrainer)
                .trainingDate(LocalDate.now())
                .trainingName("Another training")
                .duration(60)
                .trainingType(null)
                .build();
        traineeDao.create(anotherTrainee);
        trainerDao.create(anotherTrainer);
        trainingDao.create(another);
        List<Training> trainings = trainingDao.selectAll();
        cleanUp(initial.getId());
        cleanUp(another.getId());
        cleanUpTrainee("Test.Trainee");
        cleanUpTrainee("Another.Trainee");
        cleanUpTrainer("Test.Trainer");
        cleanUpTrainer("Another.Trainer");
        assertFalse(trainings.isEmpty());
        trainings.forEach(training -> {
            if (training.getId() == initial.getId()) {
                assertEquals(initial, training);
            } else if (training.getId() == another.getId()) {
                assertEquals(another, training);
            }
        });
    }

    @Test
    void testSelectAllThrowsException() {
        SessionFactory mockSessionFactory = mock(SessionFactory.class);
        Session mockSession = mock(Session.class);
        doThrow(HibernateException.class).when(mockSessionFactory).openSession();
        doReturn(mockSession).when(mockSessionFactory).getCurrentSession();
        doNothing().when(mockSession).close();
        ReflectionTestUtils.setField(trainingDao, "sessionFactory", mockSessionFactory);
        assertThrows(HibernateException.class, () -> trainingDao.selectAll());
    }

    @Test
    void testSelectAllThrowsTrainingNotFoundException() {
        assertThrows(NoExpectedDataInDatabaseException.class, () -> trainingDao.selectAll());
    }
}
