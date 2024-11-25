package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.RequestParams;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MicroserviceClientServiceImpl implements MicroserviceClientService {
    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    @Override
    public ResponseEntity<Void> submitWorkloadChanges(RequestParams requestParams) {
        String url = eurekaClient.getNextServerFromEureka("microservice", false)
                .getHomePageUrl() + "api/v1/workload/submit";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", requestParams.getUsername());
        requestBody.put("firstName", requestParams.getFirstName());
        requestBody.put("lastName", requestParams.getLastName());
        requestBody.put("isActive", requestParams.getIsActive());
        requestBody.put("date", requestParams.getDate());
        requestBody.put("duration", requestParams.getDuration());
        requestBody.put("type", requestParams.getType());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);
        return restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                requestEntity,
                Void.class
        );
    }

    @Override
    public boolean isServiceAvailable(String serviceName) {
        try {
            Application application = eurekaClient.getApplication(serviceName);
            return application != null && !application.getInstances().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
