package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.Dao;
import com.epam.gymsystem.domain.Token;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Dao
@AllArgsConstructor
public class BlacklistDaoImpl implements BlacklistDao {
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void blacklist(Token token) {
        entityManager.persist(token);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        long count = entityManager.createQuery("select count(t) from Token t where id = :id", Long.class)
                .setParameter("id", token)
                .getSingleResult();
        return count > 0;
    }
}
