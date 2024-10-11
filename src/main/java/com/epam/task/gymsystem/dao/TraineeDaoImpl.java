package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.Dao;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Dao
public class TraineeDaoImpl implements TraineeDao {
    private final EntityManager entityManager;

    @Autowired
    public TraineeDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Trainee trainee) {
        entityManager.persist(trainee);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        entityManager.createQuery("update Trainee set password = :newPassword where username = :username")
                .setParameter("newPassword", newPassword)
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    public void update(String username, Trainee updated) {
        updated.setId(entityManager.createQuery("select id from Trainee where username = :username", Integer.class)
                .setParameter("username", username)
                .getSingleResult());
        entityManager.merge(updated);
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        entityManager.createQuery("update Trainee set isActive = :newActivityStatus where username = :username")
                .setParameter("newActivityStatus", newActivityStatus)
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    public List<Training> selectTrainings(Trainee trainee, TrainingCriteria criteria) {
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
        return entityManager.createQuery("from Trainer where username in :usernames", Trainer.class)
                .setParameter("usernames", trainersUsernames)
                .getResultList();
    }

    @Override
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
    }

    @Override
    public void delete(String username) {
        Trainee trainee = entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                .setParameter("username", username)
                .getSingleResult();
        entityManager.refresh(trainee);
        entityManager.remove(trainee);
    }

    @Override
    public Trainee select(String username) {
        Trainee trainee;
        try {
            trainee = entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("Trainee with username " + username + " was not found");
        }
        return trainee;
    }

    @Override
    public List<Trainee> selectAll() {
        return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class).getResultList();
    }

    @Override
    public List<String> selectUsernames() {
        return entityManager.createQuery("SELECT username FROM Trainee", String.class).getResultList();
    }
}
