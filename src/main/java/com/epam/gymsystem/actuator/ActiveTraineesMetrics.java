package com.epam.gymsystem.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnEnabledMetricsExport("active_trainees_counter")
public final class ActiveTraineesMetrics {
    private final EntityManager entityManager;
    private final MeterRegistry meterRegistry;

    @Scheduled(fixedRate = 60000)
    public void updateTraineesCount() {
        meterRegistry.gauge("active_trainees_counter", countActiveTrainees());
    }

    private long countActiveTrainees() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.isActive = true", Long.class)
                .getSingleResult();
    }
}
