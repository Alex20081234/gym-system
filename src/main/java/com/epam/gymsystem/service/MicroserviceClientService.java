package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.RequestParams;
import org.springframework.http.ResponseEntity;

public interface MicroserviceClientService {
    ResponseEntity<Void> submitWorkloadChanges(RequestParams requestParams);

    boolean isServiceAvailable(String serviceName);
}
