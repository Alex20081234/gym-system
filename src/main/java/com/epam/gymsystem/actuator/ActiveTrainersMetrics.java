package com.epam.gymsystem.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnEnabledMetricsExport("active_trainers_counter")
public class ActiveTrainersMetrics {
    private final EntityManager entityManager;
    private final MeterRegistry meterRegistry;

    private long countActiveTrainers() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Trainer t WHERE t.isActive = true", Long.class)
                .getSingleResult();
    }

    @Scheduled(fixedRate = 60000)
    public void updateTrainersCount() {
        meterRegistry.gauge("active_trainers_counter", countActiveTrainers());
    }
}
