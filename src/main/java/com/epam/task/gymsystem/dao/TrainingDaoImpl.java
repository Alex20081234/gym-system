package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.Dao;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.domain.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Dao
public class TrainingDaoImpl implements TrainingDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDaoImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(Training training) {
        entityManager.persist(training);
        LOGGER.info("Training {} was successfully created", training);
    }

    @Override
    @Transactional(readOnly = true)
    public Training select(int trainingId) {
        Training training;
        try {
            training = entityManager.createQuery("SELECT t FROM Training t WHERE t.id = :id", Training.class)
                    .setParameter("id", trainingId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new TrainingNotFoundException("Training with id " + trainingId + " was not found");
        }
        LOGGER.info("Training with id {} was successfully selected", trainingId);
        return training;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> selectAll() {
        LOGGER.info("Successfully selected all trainings");
        return entityManager.createQuery("SELECT t FROM Training t", Training.class).getResultList();
    }
}
