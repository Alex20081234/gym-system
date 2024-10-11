package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.Dao;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

@Dao
public class TrainerDaoImpl implements TrainerDao {
    private final EntityManager entityManager;

    public TrainerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Trainer trainer) {
        entityManager.persist(trainer);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainer trainer = entityManager.createQuery("select t from Trainer t where t.username = :username", Trainer.class)
                .setParameter("username", username)
                .getSingleResult();
        trainer.setPassword(newPassword);
        entityManager.merge(trainer);
    }

    @Override
    public String update(String username, Trainer updated) {
        entityManager.merge(updated);
        return updated.getUsername();
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        Trainer trainer = entityManager.createQuery("select t from Trainer t where t.username = :username", Trainer.class)
                .setParameter("username", username)
                .getSingleResult();
        trainer.setIsActive(newActivityStatus);
        entityManager.merge(trainer);
    }

    @Override
    public List<Training> selectTrainings(Trainer trainer, TrainingCriteria criteria) {
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
    public Trainer select(String username) {
        Trainer trainer;
        try {
            trainer = entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("Trainer with username " + username + " was not found");
        }
        return trainer;
    }

    @Override
    public List<Trainer> selectAll() {
        return entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class).getResultList();
    }

    @Override
    public List<String> selectUsernames() {
        return entityManager.createQuery("SELECT u.username FROM User u", String.class).getResultList();
    }

    @Override
    public void loadDependencies(Trainer trainer) {
        entityManager.refresh(trainer);
    }
}
