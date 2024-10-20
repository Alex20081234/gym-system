package com.epam.gymsystem.actuator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DbDataHealthIndicatorTest {
    private EntityManager entityManager;
    private DbDataHealthIndicator dbDataHealthIndicator;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        dbDataHealthIndicator = new DbDataHealthIndicator(entityManager);
    }

    @Test
    void healthShouldReturnUpWhenDataExists() {
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT COUNT(t) from TrainingType t", Long.class))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(5L);
        Health health = dbDataHealthIndicator.health();
        assertEquals(Health.up().withDetail("Training Types", "Data is present").build(), health);
    }

    @Test
    void healthShouldReturnDownWhenDataNonexistent() {
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT COUNT(t) from TrainingType t", Long.class))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0L);
        Health health = dbDataHealthIndicator.health();
        assertEquals(Health.down().withDetail("Training Types", "Data is missing").build(), health);
    }
}
