package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.Dao;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Dao
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeDaoImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(Trainee trainee) {
        entityManager.persist(trainee);
        LOGGER.info("Trainee {} was successfully created", trainee);
    }

    @Override
    @Transactional
    public void changePassword(Trainee trainee, String newPassword) {
        trainee.setPassword(newPassword);
        entityManager.merge(trainee);
        LOGGER.info("Trainee {}'s password was successfully changed", trainee.getUsername());
    }

    @Override
    @Transactional
    public void update(Trainee trainee, Trainee updated) {
        updated.setId(trainee.getId());
        entityManager.merge(updated);
        LOGGER.info("Trainee {} was successfully updated", trainee.getUsername());
    }

    @Override
    @Transactional
    public void changeActivityStatus(Trainee trainee, boolean newActivityStatus) {
        trainee.setIsActive(newActivityStatus);
        entityManager.merge(trainee);
        LOGGER.info("Trainee {}'s activity status was successfully changed", trainee.getUsername());
    }

    @Override
    public List<Training> selectTrainings(Trainee trainee, TrainingCriteria criteria) {
        LOGGER.info("Successfully selected trainings for trainee {}", trainee.getUsername());
        entityManager.refresh(trainee);
        if (criteria == null) {
            return trainee.getTrainings();
        }
        return trainee.getTrainings().stream().filter(training -> {
            if (criteria.getFromDate() != null && training.getTrainingDate().isBefore(criteria.getFromDate())) {
                return false;
            }
            if (criteria.getToDate() != null && training.getTrainingDate().isAfter(criteria.getToDate())) {
                return false;
            }
            if (criteria.getPartnerUsername() != null && !training.getTrainer().getUsername().equals(criteria.getPartnerUsername())) {
                return false;
            }
            return criteria.getTrainingType() == null || training.getTrainingType().equals(criteria.getTrainingType());
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> selectNotAssignedTrainers(Trainee trainee) {
        List<String> trainersUsernames = entityManager.createQuery("select username from Trainer", String.class).getResultList();
        if (trainersUsernames.isEmpty()) {
            return Collections.emptyList();
        }
        entityManager.refresh(trainee);
        List<String> assignedTrainersUsernames = trainee.getTrainings().stream()
                .map(training -> training.getTrainer().getUsername())
                .toList();
        trainersUsernames.removeAll(assignedTrainersUsernames);
        LOGGER.info("Trainers that are not assigned to trainee were successfully selected");
        return entityManager.createQuery("from Trainer where username in :usernames", Trainer.class)
                .setParameter("usernames", trainersUsernames)
                .getResultList();
    }

    @Override
    @Transactional
    public void updateTrainers(Trainee trainee, Map<String, Boolean> trainerUsernames) {
        Set<Trainer> trainersToAdd = new HashSet<>();
        Set<Trainer> trainersToRemove = new HashSet<>();
        Trainer trainer;
        for (String current : trainerUsernames.keySet()) {
            try {
                trainer = entityManager.createQuery("from Trainer where username = :username", Trainer.class)
                        .setParameter("username", current)
                        .getSingleResult();
            } catch (NoResultException e) {
                throw new UserNotFoundException("Trainer with username " + current + " was not found");
            }
            if (trainerUsernames.get(current)) {
                trainersToAdd.add(trainer);
            } else {
                trainersToRemove.add(trainer);
            }
        }
        entityManager.refresh(trainee);
        trainee.setTrainers(trainersToAdd);
        trainee.removeTrainers(trainersToRemove);
        entityManager.merge(trainee);
        LOGGER.info("Trainers were successfully updated");
    }

    @Override
    @Transactional
    public void delete(String username) {
        Trainee trainee = entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                .setParameter("username", username)
                .getSingleResult();
        entityManager.refresh(trainee);
        entityManager.remove(trainee);
        LOGGER.info("Trainee {} was successfully deleted", trainee.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee select(String username) {
        Trainee trainee;
        try {
            trainee = entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("Trainee with username " + username + " was not found");
        }
        LOGGER.info("Trainee with username {} was successfully selected", username);
        return trainee;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> selectAll() {
        LOGGER.info("Successfully selected all trainees");
        return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class).getResultList();
    }
}
