package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerDaoImpl.class);
    private final SessionFactory sessionFactory;
    private static final String NOT_FOUND_MESSAGE = "Trainer with username {} was not found";

    @Autowired
    public TrainerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainer trainer) {
        LOGGER.info("Starting creation of trainer: {}", trainer);
        if (trainer.getUsername() == null) {
            LOGGER.error("Trainer's username is null");
            throw new IllegalArgumentException("Trainer's username is null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(trainer);
            transaction.commit();
            LOGGER.info("Trainer was successfully created");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to create trainer: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public void changePassword(String username, String newPassword) throws UserNotFoundException {
        LOGGER.info("Changing password for trainer with username: {}", username);
        if (newPassword == null || newPassword.isEmpty()) {
            LOGGER.error("New password is null or empty");
            throw new IllegalArgumentException("New password is null or empty");
        }
        Trainer trainer = select(username);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainer.setPassword(newPassword);
            session.merge(trainer);
            transaction.commit();
            LOGGER.info("Password was successfully changed");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to change password: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public void update(String username, Trainer updated) throws UserNotFoundException {
        LOGGER.info("Updating trainer with username: {}", username);
        if (updated.getUsername() == null) {
            LOGGER.error("Trainer's username is null");
            throw new IllegalArgumentException("Trainer's username is null");
        }
        Trainer trainer = select(username);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            updated.setId(trainer.getId());
            session.merge(updated);
            transaction.commit();
            LOGGER.info("Trainer was successfully updated");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to update trainer: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) throws UserNotFoundException, ActivityStatusAlreadyExistsException {
        LOGGER.info("Changing activity status for trainer with username: {}", username);
        Trainer trainer = select(username);
        if (trainer.getIsActive() == newActivityStatus) {
            LOGGER.error("Trainer with username {} already has activity status {}", username, newActivityStatus);
            throw new ActivityStatusAlreadyExistsException("Trainer with username " + username + " already has activity status " + newActivityStatus);
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainer.setIsActive(newActivityStatus);
            session.merge(trainer);
            transaction.commit();
            LOGGER.info("Activity status was successfully changed");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to change activity status: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public List<Training> selectTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeUsername,
                                          TrainingType trainingType) throws UserNotFoundException {
        LOGGER.info("Selecting trainings with some criteria for trainer with username: {}", username);
        List<Training> selectedTrainings;
        Trainer trainer = select(username);
        selectedTrainings = trainer.getTrainings().stream().filter(training -> {
            if (fromDate != null && training.getTrainingDate().isBefore(fromDate)) {
                return false;
            }
            if (toDate != null && training.getTrainingDate().isAfter(toDate)) {
                return false;
            }
            if (traineeUsername != null && !training.getTrainee().getUsername().equals(traineeUsername)) {
                return false;
            }
            return trainingType == null || training.getTrainingType().equals(trainingType);
        }).toList();
        LOGGER.info("Successfully selected trainings for trainer with username: {}", username);
        return selectedTrainings;
    }

    @Override
    public Trainer select(String username) throws UserNotFoundException {
        LOGGER.info("Selecting trainer with username: {}", username);
        Trainer trainer;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainer = session.createQuery("from Trainer where username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainer == null) {
                LOGGER.error(NOT_FOUND_MESSAGE, username);
                throw new UserNotFoundException("Trainer with username " + username + " was not found");
            }
            transaction.commit();
            LOGGER.info("Trainer was successfully selected");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select trainer: {}", e.getMessage());
            throw e;
        } catch (UserNotFoundException e) {
            LOGGER.error("Failed to select trainer: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return trainer;
    }

    @Override
    public List<Trainer> selectAll() throws NoExpectedDataInDatabaseException {
        LOGGER.info("Selecting all trainers");
        List<Trainer> trainers;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainers = session.createQuery("from Trainer", Trainer.class).list();
            if (trainers.isEmpty()) {
                LOGGER.error("No trainers were found");
                throw new NoExpectedDataInDatabaseException("No trainers were found");
            }
            transaction.commit();
            LOGGER.info("All trainers were successfully selected");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select all trainers: {}", e.getMessage());
            throw e;
        } catch (NoExpectedDataInDatabaseException e) {
            LOGGER.error("Failed to select all trainers: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return trainers;
    }
}
