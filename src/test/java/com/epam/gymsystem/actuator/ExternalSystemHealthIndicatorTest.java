package com.epam.gymsystem.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.AbstractMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExternalSystemHealthIndicatorTest {
    private ExternalSystemHealthIndicator externalSystemHealthIndicator;
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        externalSystemHealthIndicator = spy(new ExternalSystemHealthIndicator(httpClient,
                new AbstractMap.SimpleEntry<>("Google", "https://www.google.com")));
    }

    @Test
    void healthShouldReturnUpWhenAvailable() throws IOException, InterruptedException {
        HttpResponse response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(httpClient.send(any(), any())).thenReturn(response);
        Health health = externalSystemHealthIndicator.health();
        assertEquals(Health.up().withDetail("Google", "Available").build(), health);
    }

    @Test
    void healthShouldReturnDownWhenUnavailable() throws IOException, InterruptedException {
        HttpResponse response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(404);
        when(httpClient.send(any(), any())).thenReturn(response);
        Health health = externalSystemHealthIndicator.health();
        assertEquals(Health.down().withDetail("Google", "Unavailable - Response Code: 404").build(), health);
        response = mock(HttpResponse.class);
        when(response.statusCode()).thenThrow(new RuntimeException("Network error"));
        when(httpClient.send(any(), any())).thenReturn(response);
        health = externalSystemHealthIndicator.health();
        assertEquals(Health.down().withDetail("Google", "Unavailable - Exception: Network error").build(), health);
    }
}
