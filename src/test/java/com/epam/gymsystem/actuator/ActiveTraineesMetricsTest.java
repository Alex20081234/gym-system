package com.epam.gymsystem.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActiveTraineesMetricsTest {
    private EntityManager entityManager;
    private MeterRegistry meterRegistry;
    private ActiveTraineesMetrics activeTraineesMetrics;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        meterRegistry = Mockito.mock(MeterRegistry.class);
        activeTraineesMetrics = new ActiveTraineesMetrics(entityManager, meterRegistry);
    }

    @Test
    void testUpdateTrainersCount() {
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.isActive = true", Long.class))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(5L);
        activeTraineesMetrics.updateTraineesCount();
        verify(meterRegistry).gauge("active_trainees_counter", 5L);
    }
}
