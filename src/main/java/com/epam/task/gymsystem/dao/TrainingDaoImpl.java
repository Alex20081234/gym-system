package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.Dao;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.domain.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

@Dao
public class TrainingDaoImpl implements TrainingDao {
    private final EntityManager entityManager;

    public TrainingDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Training training) {
        entityManager.persist(training);
    }

    @Override
    public Training select(int trainingId) {
        Training training;
        try {
            training = entityManager.createQuery("SELECT t FROM Training t WHERE t.id = :id", Training.class)
                    .setParameter("id", trainingId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new TrainingNotFoundException("Training with id " + trainingId + " was not found");
        }
        return training;
    }

    @Override
    public List<Training> selectAll() {
        return entityManager.createQuery("SELECT t FROM Training t", Training.class).getResultList();
    }
}
