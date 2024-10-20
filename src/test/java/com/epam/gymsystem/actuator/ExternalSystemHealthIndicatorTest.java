package com.epam.gymsystem.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExternalSystemHealthIndicatorTest {
    private ExternalSystemHealthIndicator externalSystemHealthIndicator;

    @BeforeEach
    void setUp() {
        externalSystemHealthIndicator = Mockito.spy(new ExternalSystemHealthIndicator());
    }

    @Test
    void healthShouldReturnUpWhenAvailable() throws IOException, InterruptedException, URISyntaxException {
        when(externalSystemHealthIndicator.checkExternalSystemAvailability()).thenReturn(200);
        Health health = externalSystemHealthIndicator.health();
        assertEquals(Health.up().withDetail("Google", "Available").build(), health);
    }

    @Test
    void healthShouldReturnDownWhenUnavailable() throws IOException, InterruptedException, URISyntaxException {
        when(externalSystemHealthIndicator.checkExternalSystemAvailability()).thenReturn(404);
        Health health = externalSystemHealthIndicator.health();
        assertEquals(Health.down().withDetail("Google", "Unavailable - Response Code: 404").build(), health);
        when(externalSystemHealthIndicator.checkExternalSystemAvailability()).thenThrow(new IOException("Network error"));
        health = externalSystemHealthIndicator.health();
        assertEquals(Health.down().withDetail("Google", "Unavailable - Exception: Network error").build(), health);
    }
}
