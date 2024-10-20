package com.epam.gymsystem.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActiveTrainersMetricsTest {
    private EntityManager entityManager;
    private MeterRegistry meterRegistry;
    private ActiveTrainersMetrics activeTrainersMetrics;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        meterRegistry = Mockito.mock(MeterRegistry.class);
        activeTrainersMetrics = new ActiveTrainersMetrics(entityManager, meterRegistry);
    }

    @Test
    void testUpdateTrainersCount() {
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT COUNT(t) FROM Trainer t WHERE t.isActive = true", Long.class))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(5L);
        activeTrainersMetrics.updateTrainersCount();
        verify(meterRegistry).gauge("active_trainers_counter", 5L);
    }
}
