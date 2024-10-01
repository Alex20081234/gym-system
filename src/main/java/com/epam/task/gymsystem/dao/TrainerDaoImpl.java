package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.Dao;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Dao
public class TrainerDaoImpl implements TrainerDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerDaoImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(Trainer trainer) {
        entityManager.persist(trainer);
        LOGGER.info("Trainer {} was successfully created", trainer);
    }

    @Override
    @Transactional
    public void changePassword(Trainer trainer, String newPassword) {
        trainer.setPassword(newPassword);
        entityManager.merge(trainer);
        LOGGER.info("Trainer {}'s password was successfully changed", trainer.getUsername());
    }

    @Override
    @Transactional
    public void update(Trainer trainer, Trainer updated) {
        updated.setId(trainer.getId());
        entityManager.merge(updated);
        LOGGER.info("Trainer {} was successfully updated", trainer.getUsername());
    }

    @Override
    @Transactional
    public void changeActivityStatus(Trainer trainer, boolean newActivityStatus) {
        trainer.setIsActive(newActivityStatus);
        entityManager.merge(trainer);
        LOGGER.info("Trainer {}'s activity status was successfully changed", trainer.getUsername());
    }

    @Override
    public List<Training> selectTrainings(Trainer trainer, TrainingCriteria criteria) {
        LOGGER.info("Successfully selected trainings for trainer {}", trainer.getUsername());
        entityManager.refresh(trainer);
        if (criteria == null) {
            return trainer.getTrainings();
        }
        return trainer.getTrainings().stream().filter(training -> {
            if (criteria.getFromDate() != null && training.getTrainingDate().isBefore(criteria.getFromDate())) {
                return false;
            }
            if (criteria.getToDate() != null && training.getTrainingDate().isAfter(criteria.getToDate())) {
                return false;
            }
            if (criteria.getPartnerUsername() != null && !training.getTrainee().getUsername().equals(criteria.getPartnerUsername())) {
                return false;
            }
            return criteria.getTrainingType() == null || training.getTrainingType().equals(criteria.getTrainingType());
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer select(String username) {
        Trainer trainer;
        try {
            trainer = entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("Trainer with username " + username + " was not found");
        }
        LOGGER.info("Trainer with username {} was successfully selected", username);
        return trainer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> selectAll() {
        LOGGER.info("Successfully selected all trainers");
        return entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class).getResultList();
    }
}
