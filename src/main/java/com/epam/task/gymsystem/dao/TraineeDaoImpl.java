package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainee;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeDaoImpl.class);
    private final SessionFactory sessionFactory;
    private static final String NOT_FOUND_MESSAGE = "Trainee with username {} was not found";

    @Autowired
    public TraineeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainee trainee) {
        LOGGER.info("Creating trainee with username: {}", trainee.getUsername());
        if (trainee.getUsername() == null) {
            LOGGER.error("Trainee username is null");
            throw new IllegalArgumentException("Trainee username is null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.persist(trainee);
            transaction.commit();
            LOGGER.info("Trainee was successfully created");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to create trainee: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public void changePassword(String username, String newPassword) throws UserNotFoundException {
        LOGGER.info("Changing password for trainee with username: {}", username);
        if (newPassword == null || newPassword.isEmpty()) {
            LOGGER.error("New password is null or empty");
            throw new IllegalArgumentException("New password is null or empty");
        }
        Trainee trainee = select(username);
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            trainee.setPassword(newPassword);
            session.merge(trainee);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to change password: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        LOGGER.info("Password was successfully changed");
    }

    @Override
    public void update(String username, Trainee updated) throws UserNotFoundException {
        LOGGER.info("Updating trainee with username: {}", username);
        if (updated.getUsername() == null) {
            LOGGER.error("Trainee username is null");
            throw new IllegalArgumentException("Trainee username is null");
        }
        Trainee trainee = select(username);
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            updated.setId(trainee.getId());
            session.merge(updated);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to update trainee: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        LOGGER.info("Trainee was successfully updated");
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) throws UserNotFoundException, ActivityStatusAlreadyExistsException {
        LOGGER.info("Changing activity status for trainee with username: {}", username);
        Trainee trainee = select(username);
        if (trainee.getIsActive() == newActivityStatus) {
            LOGGER.error("Trainee with username {} already has activity status {}", username, newActivityStatus);
            throw new ActivityStatusAlreadyExistsException("Trainee with username " + username + " already has activity status " + newActivityStatus);
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            trainee.setIsActive(newActivityStatus);
            session.merge(trainee);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to change activity status: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        LOGGER.info("Activity status was successfully changed");
    }

    @Override
    public List<Training> selectTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerUsername,
                                          TrainingType trainingType) throws UserNotFoundException {
        LOGGER.info("Selecting trainings with some criteria for trainee with username: {}", username);
        List<Training> selectedTrainings;
        Trainee trainee = select(username);
        selectedTrainings = trainee.getTrainings().stream().filter(training -> {
            if (fromDate != null && training.getTrainingDate().isBefore(fromDate)) {
                return false;
            }
            if (toDate != null && training.getTrainingDate().isAfter(toDate)) {
                return false;
            }
            if (trainerUsername != null && !training.getTrainer().getUsername().equals(trainerUsername)) {
                return false;
            }
            return trainingType == null || training.getTrainingType().equals(trainingType);
        }).toList();
        LOGGER.info("Successfully selected trainings for trainee with username: {}", username);
        return selectedTrainings;
    }

    @Override
    public List<Trainer> selectNotAssignedTrainers(String username) throws NoExpectedDataInDatabaseException, UserNotFoundException {
        LOGGER.info("Selecting trainers that are not assigned to trainee with username: {}", username);
        Trainee trainee = select(username);
        List<Trainer> notAssignedTrainers;
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            List<String> trainersUsernames = session.createQuery("select username from Trainer", String.class).list();
            if (trainersUsernames.isEmpty()) {
                LOGGER.error("There are no trainers in the database");
                throw new NoExpectedDataInDatabaseException("There are no trainers in the database");
            }
            List<String> assignedTrainersUsernames = trainee.getTrainings().stream()
                    .map(training -> training.getTrainer().getUsername())
                    .toList();
            trainersUsernames.removeAll(assignedTrainersUsernames);
            notAssignedTrainers = session.createQuery("from Trainer where username in (:usernames)", Trainer.class)
                    .setParameter("usernames", trainersUsernames)
                    .list();
            transaction.commit();
            LOGGER.info("Successfully selected trainers not assigned to trainee with username: {}", username);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select trainers not assigned: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return notAssignedTrainers;
    }

    @Override
    public void updateTrainers(String username, Map<String, Boolean> trainerUsernames) throws UserNotFoundException {
        LOGGER.info("Updating trainers with usernames {} to trainee with username: {}", trainerUsernames, username);
        Trainee trainee = select(username);
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession();) {
            transaction = session.beginTransaction();
            Set<Trainer> trainersToAdd = new HashSet<>();
            Set<Trainer> trainersToRemove = new HashSet<>();
            for (String current : trainerUsernames.keySet()) {
                Trainer trainer = session.createQuery("from Trainer where username = :username", Trainer.class)
                        .setParameter("username", current)
                        .uniqueResult();
                if (trainer == null) {
                    LOGGER.error("Trainer with username {} was not found", current);
                    throw new UserNotFoundException("Trainer with username " + current + " was not found");
                }
                if (Boolean.TRUE.equals(trainerUsernames.get(current))) {
                    trainersToAdd.add(trainer);
                } else {
                    trainersToRemove.add(trainer);
                }
            }
            trainee.setTrainers(trainersToAdd);
            trainee.removeTrainers(trainersToRemove);
            session.merge(trainee);
            transaction.commit();
            LOGGER.info("Trainers were successfully updated");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to update trainers: {}", e.getMessage());
            throw e;
        } catch (UserNotFoundException e) {
            LOGGER.error("Failed to update trainers: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public void delete(String username) throws UserNotFoundException {
        LOGGER.info("Deleting trainee with username: {}", username);
        Trainee trainee = select(username);
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.remove(trainee);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to delete trainee: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        LOGGER.info("Trainee was successfully deleted");
    }

    @Override
    public Trainee select(String username) throws UserNotFoundException {
        LOGGER.info("Selecting trainee with username: {}", username);
        Trainee trainee;
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            trainee = session.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainee == null) {
                LOGGER.error(NOT_FOUND_MESSAGE, username);
                throw new UserNotFoundException("Trainee with username " + username + " was not found");
            }
            transaction.commit();
            LOGGER.info("Trainee was successfully selected");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select trainee: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return trainee;
    }

    @Override
    public List<Trainee> selectAll() throws NoExpectedDataInDatabaseException {
        LOGGER.info("Selecting all trainees");
        List<Trainee> trainees;
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            trainees = session.createQuery("from Trainee", Trainee.class).list();
            if (trainees.isEmpty()) {
                LOGGER.error("There are no trainees in the database");
                throw new NoExpectedDataInDatabaseException("There are no trainees in the database");
            }
            transaction.commit();
            LOGGER.info("All trainees were successfully selected");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select all trainees: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return trainees;
    }
}
