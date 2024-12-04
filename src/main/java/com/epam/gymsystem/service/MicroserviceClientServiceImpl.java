package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.SubmitWorkloadChangesRequestBody;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Void> submitWorkloadChanges(SubmitWorkloadChangesRequestBody requestParams, String token) {
        String url = eurekaClient.getNextServerFromEureka("microservice", false)
                .getHomePageUrl() + "api/v1/workload/submit";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("trainerUsername", requestParams.getTrainerUsername());
        requestBody.put("trainerFirstName", requestParams.getTrainerFirstName());
        requestBody.put("trainerLastName", requestParams.getTrainerLastName());
        requestBody.put("trainerIsActive", requestParams.getTrainerIsActive());
        requestBody.put("trainingDate", requestParams.getTrainingDate());
        requestBody.put("trainingDurationMinutes", requestParams.getTrainingDurationMinutes());
        requestBody.put("changeType", requestParams.getChangeType());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
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
