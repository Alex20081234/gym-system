package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.SubmitWorkloadChangesRequestBody;

public interface MessageSenderService {
    void sendMessage(SubmitWorkloadChangesRequestBody requestBody);
}
