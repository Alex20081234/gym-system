package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.ActionType;
import com.epam.gymsystem.dto.RequestParams;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MicroserviceClientServiceImplTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EurekaClient eurekaClient;
    @InjectMocks
    private MicroserviceClientServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitChangesShouldTryToSubmitChanges() {
        RequestParams params = RequestParams.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .type(ActionType.ADD)
                .date(LocalDate.of(2024, 10, 12))
                .duration(10)
                .build();
        InstanceInfo info = mock(InstanceInfo.class);
        when(eurekaClient.getNextServerFromEureka(anyString(), anyBoolean())).thenReturn(info);
        when(info.getHomePageUrl()).thenReturn("/home-page/");
        when(restTemplate.exchange(anyString(), any(), any(), (Class<Object>) any()))
                .thenReturn(ResponseEntity.noContent().build());
        service.submitWorkloadChanges(params);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", params.getUsername());
        requestBody.put("firstName", params.getFirstName());
        requestBody.put("lastName", params.getLastName());
        requestBody.put("isActive", params.getIsActive());
        requestBody.put("date", params.getDate());
        requestBody.put("duration", params.getDuration());
        requestBody.put("type", params.getType());
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody);
        verify(restTemplate, times(1)).exchange("/home-page/api/v1/workload/submit",
                HttpMethod.PATCH,
                httpEntity,
                Void.class);
    }

    @Test
    void isServiceAvailableShouldReturnTrueWhenServiceAvailable() {
        Application app = mock(Application.class);
        when(eurekaClient.getApplication(anyString())).thenReturn(app);
        List<InstanceInfo> list = mock(List.class);
        when(app.getInstances()).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        assertTrue(service.isServiceAvailable("microservice"));
    }

    @Test
    void isServiceAvailableShouldReturnFalseWhenServerIsUnavailable() {
        when(eurekaClient.getApplication(anyString())).thenReturn(null);
        assertFalse(service.isServiceAvailable("microservice"));
        Application app = mock(Application.class);
        when(eurekaClient.getApplication(anyString())).thenReturn(app);
        List<InstanceInfo> list = mock(List.class);
        when(app.getInstances()).thenReturn(list);
        when(list.isEmpty()).thenReturn(true);
        assertFalse(service.isServiceAvailable("microservice"));
        when(eurekaClient.getApplication(anyString())).thenThrow(new RuntimeException());
        assertFalse(service.isServiceAvailable("microservice"));
    }
}
