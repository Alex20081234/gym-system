package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.SubmitWorkloadChangesRequestBody;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageSenderServiceImpl implements MessageSenderService {
    private final JmsTemplate jmsTemplate;
    @Qualifier("queueName")
    private final String queueName;

    @Override
    public void sendMessage(SubmitWorkloadChangesRequestBody requestBody) {
        jmsTemplate.convertAndSend(queueName, requestBody);
    }
}
