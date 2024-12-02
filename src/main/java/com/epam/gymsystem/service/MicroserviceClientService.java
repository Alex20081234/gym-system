package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.SubmitWorkloadChangesRequestBody;
import org.springframework.http.ResponseEntity;

public interface MicroserviceClientService {
    ResponseEntity<Void> submitWorkloadChanges(SubmitWorkloadChangesRequestBody requestParams, String token);

    boolean isServiceAvailable(String serviceName);
}
