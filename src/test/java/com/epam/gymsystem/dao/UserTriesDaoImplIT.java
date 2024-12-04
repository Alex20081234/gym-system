package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.UserTries;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserTriesDaoImplIT {
    private static final String USERNAME = "Test.User";
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserTriesDaoImpl dao;
    private boolean skip = false;

    @BeforeEach
    void setUp() {
        skip = false;
    }

    @AfterEach
    void cleanUp() {
        if (skip) return;
        UserTries tries = entityManager.find(UserTries.class, USERNAME);
        entityManager.remove(tries);
        ReflectionTestUtils.setField(dao, "lockTime", 300000);
    }

    @Test
    void incrementAttemptsOrCreateEntryShouldIncrementWhenExists() {
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        assertEquals(2, dao.attempts(USERNAME));
    }

    @Test
    void incrementAttemptsOrCreateEntryShouldCreateNewEntryWhenNonexistent() {
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        assertEquals(1, dao.attempts(USERNAME));
    }

    @Test
    void blockShouldSetBlockDate() {
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        dao.block(USERNAME);
        assertTrue(dao.isBlocked(USERNAME));
    }

    @Test
    void isBlockedShouldReturnTrueWhenBlockIsNotOver() {
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        dao.block(USERNAME);
        assertTrue(dao.isBlocked(USERNAME));
    }

    @Test
    void isBlockedShouldReturnFalseWhenBlockIsOver() {
        ReflectionTestUtils.setField(dao, "lockTime", 0);
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        assertFalse(dao.isBlocked(USERNAME));
        dao.block(USERNAME);
        assertFalse(dao.isBlocked(USERNAME));
    }

    @Test
    void isBlockedShouldReturnFalseWhenNoBlock() {
        skip = true;
        assertFalse(dao.isBlocked("Non.Existent"));
    }

    @Test
    void attemptsShouldReturnCurrentUsedAttempts() {
        dao.incrementAttemptsOrCreateEntry(USERNAME);
        assertEquals(1, dao.attempts(USERNAME));
    }
}
