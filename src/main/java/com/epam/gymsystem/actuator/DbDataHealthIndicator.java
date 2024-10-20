package com.epam.gymsystem.actuator;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnEnabledHealthIndicator("db_data")
public class DbDataHealthIndicator implements HealthIndicator {
    private EntityManager entityManager;

    @Override
    public Health health() {
        boolean typesExist = checkTrainingTypesExistence();
        if (typesExist) {
            return Health.up().withDetail("Training Types", "Data is present").build();
        } else {
            return Health.down().withDetail("Training Types", "Data is missing").build();
        }
    }

    private boolean checkTrainingTypesExistence() {
        Long count = entityManager.createQuery("SELECT COUNT(t) from TrainingType t", Long.class).getSingleResult();
        return count > 0;
    }
}
