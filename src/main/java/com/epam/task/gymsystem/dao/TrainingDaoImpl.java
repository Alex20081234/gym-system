package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDaoImpl.class);
    private final SessionFactory sessionFactory;

    @Autowired
    public TrainingDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Training training) throws UserNotFoundException {
        LOGGER.info("Starting creation of a training: {}", training);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Trainee trainee = session.createQuery("from Trainee where id = :id", Trainee.class)
                    .setParameter("id", training.getTrainee().getId())
                    .uniqueResult();
            if (trainee == null) {
                LOGGER.error("Trainee with id {} was not found", training.getTrainee().getId());
                throw new UserNotFoundException("Trainee with id " + training.getTrainee().getId() + " was not found");
            }
            Trainer trainer = session.createQuery("from Trainer where id = :id", Trainer.class)
                    .setParameter("id", training.getTrainer().getId())
                    .uniqueResult();
            if (trainer == null) {
                LOGGER.error("Trainer with id {} was not found", training.getTrainer().getId());
                throw new UserNotFoundException("Trainer with id " + training.getTrainer().getId() + " was not found");
            }
            session.persist(training);
            trainee.setTrainers(Set.of(trainer));
            session.merge(trainee);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to create training: {}", e.getMessage());
            throw e;
        } catch (UserNotFoundException e) {
            LOGGER.error("Failed to create training: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        LOGGER.info("Training was successfully created");
    }

    @Override
    public Training select(int trainingId) throws TrainingNotFoundException {
        LOGGER.info("Selecting training with id: {}", trainingId);
        Training training;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            training = session.get(Training.class, trainingId);
            if (training == null) {
                LOGGER.error("Training with id {} was not found", trainingId);
                throw new TrainingNotFoundException("Training with id " + trainingId + " was not found");
            }
            LOGGER.info("Training with id {} was successfully selected", trainingId);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select training with id {}: {}", trainingId, e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return training;
    }

    @Override
    public List<Training> selectAll() throws NoExpectedDataInDatabaseException {
        LOGGER.info("Selecting all trainings");
        List<Training> trainings;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainings = session.createQuery("from Training", Training.class).list();
            if (trainings.isEmpty()) {
                LOGGER.error("No trainings were found");
                throw new NoExpectedDataInDatabaseException("No trainings were found");
            }
            transaction.commit();
            LOGGER.info("All trainings were successfully selected");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to select all trainings: {}", e.getMessage());
            throw e;
        } finally {
            sessionFactory.getCurrentSession().close();
        }
        return trainings;
    }
}
