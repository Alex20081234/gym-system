package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.Dao;
import com.epam.gymsystem.domain.UserTries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Dao
@RequiredArgsConstructor
public class UserTriesDaoImpl implements UserTriesDao {
    private final EntityManager entityManager;

    @Value("${gym-system.lockTime}")
    private long lockTime;

    @Override
    @Transactional
    public void incrementAttemptsOrCreateEntry(String username) {
        UserTries userTries = entityManager.find(UserTries.class, username);
        if (userTries != null) {
            userTries.setAttempts(userTries.getAttempts() + 1);
        } else {
            userTries = UserTries.builder()
                    .id(username)
                    .attempts(1)
                    .build();
            entityManager.persist(userTries);
        }
    }

    @Override
    @Transactional
    public void block(String username) {
        UserTries userTries = entityManager.find(UserTries.class, username);
        userTries.setAttempts(0);
        userTries.setBlockTime(System.currentTimeMillis());
        userTries.setExpireDate(LocalDate.ofEpochDay(LocalDate.now().toEpochDay() + lockTime));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBlocked(String username) {
        long blockTime;
        try {
             blockTime = Optional.ofNullable(entityManager.createQuery(
                            "select u.blockTime from UserTries u where u.id = :id", Long.class)
                    .setParameter("id", username)
                    .getSingleResult()).orElse(0L);
             return blockTime != 0 && (System.currentTimeMillis() - blockTime) < lockTime;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int attempts(String username) {
        return entityManager.find(UserTries.class, username).getAttempts();
    }
}
