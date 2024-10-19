package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.common.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import java.util.*;

@Dao
@AllArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    private static final String NOT_FOUND = "Trainee with username %s was not found";
    private final EntityManager entityManager;

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
    public String update(String username, Trainee updated) {
        entityManager.merge(updated);
        return updated.getUsername();
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        entityManager.createQuery("update Trainee set isActive = :newActivityStatus where username = :username")
                .setParameter("newActivityStatus", newActivityStatus)
                .setParameter("username", username)
                .executeUpdate();
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
        return entityManager.createQuery("from Trainer where username in :usernames and isActive = true", Trainer.class)
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
        Trainee trainee;
        try {
            trainee = entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException(String.format(NOT_FOUND, username));
        }
        entityManager.refresh(trainee);
        entityManager.remove(trainee);
    }

    @Override
    public Optional<Trainee> select(String username) {
        try {
            return Optional.ofNullable(entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Trainee> selectAll() {
        return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class).getResultList();
    }

    @Override
    public List<String> selectUsernames() {
        return entityManager.createQuery("SELECT u.username FROM User u", String.class).getResultList();
    }

    @Override
    public void loadDependencies(Trainee trainee) {
        entityManager.refresh(trainee);
    }
}
