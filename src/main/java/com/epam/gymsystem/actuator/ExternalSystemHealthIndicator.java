package com.epam.gymsystem.actuator;

import lombok.AllArgsConstructor;
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
import java.util.Map;

@Component
@AllArgsConstructor
@ConditionalOnEnabledHealthIndicator("external_system")
public final class ExternalSystemHealthIndicator implements HealthIndicator {
    private final HttpClient httpClient;
    private final Map.Entry<String, String> info;

    @Override
    public Health health() {
        try {
            int responseCode = checkExternalSystemAvailability();
            if (responseCode == 200) {
                return Health.up().withDetail(info.getKey(), "Available").build();
            } else {
                return Health.down().withDetail(info.getKey(), "Unavailable - Response Code: " + responseCode).build();
            }
        } catch (Exception e) {
            return Health.down().withDetail(info.getKey(), "Unavailable - Exception: " + e.getMessage()).build();
        }
    }

    private int checkExternalSystemAvailability() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(info.getValue()))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        return response.statusCode();
    }
}
