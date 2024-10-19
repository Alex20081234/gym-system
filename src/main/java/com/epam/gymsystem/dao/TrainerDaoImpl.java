package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.common.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Optional;

@Dao
@AllArgsConstructor
public class TrainerDaoImpl implements TrainerDao {
    private static final String NOT_FOUND = "Trainer with username %s was not found";
    private final EntityManager entityManager;

    @Override
    public void create(Trainer trainer) {
        entityManager.persist(trainer);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainer trainer;
        try {
            trainer = entityManager.createQuery("select t from Trainer t where t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException(String.format(NOT_FOUND, username));
        }
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
        Trainer trainer;
        try {
            trainer = entityManager.createQuery("select t from Trainer t where t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException(String.format(NOT_FOUND, username));
        }
        trainer.setIsActive(newActivityStatus);
        entityManager.merge(trainer);
    }

    @Override
    public Optional<Trainer> select(String username) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
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
