package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.Token;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BlacklistDaoImplIT {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BlacklistDaoImpl dao;

    void cleanUp(Token token) {
        entityManager.remove(token);
    }

    @Test
    void blacklistShouldBlacklistToken() {
        Token token = Token.builder()
                .id("TestToken")
                .expireDate(LocalDate.now())
                .build();
        dao.blacklist(token);
        assertTrue(dao.isBlacklisted("TestToken"));
        cleanUp(token);
    }

    @Test
    void isBlacklistedShouldReturnTrueIfListed() {
        Token token = Token.builder()
                .id("TestToken")
                .expireDate(LocalDate.now())
                .build();
        dao.blacklist(token);
        assertTrue(dao.isBlacklisted("TestToken"));
        cleanUp(token);
    }

    @Test
    void isBlacklistedShouldReturnFalseIfNotListed() {
        assertFalse(dao.isBlacklisted("TestToken"));
    }
}
