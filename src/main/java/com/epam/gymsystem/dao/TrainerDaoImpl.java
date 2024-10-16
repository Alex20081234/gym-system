package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.common.Dao;
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
