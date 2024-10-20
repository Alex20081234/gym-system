package com.epam.gymsystem.actuator;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
@ConditionalOnEnabledHealthIndicator("external_system")
public class ExternalSystemHealthIndicator implements HealthIndicator {
    private static final String GOOGLE_URL = "https://www.google.com";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Override
    public Health health() {
        try {
            int responseCode = checkExternalSystemAvailability();
            if (responseCode == 200) {
                return Health.up().withDetail("Google", "Available").build();
            } else {
                return Health.down().withDetail("Google", "Unavailable - Response Code: " + responseCode).build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("Google", "Unavailable - Exception: " + e.getMessage()).build();
        }
    }

    protected int checkExternalSystemAvailability() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GOOGLE_URL))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        return response.statusCode();
    }
}
