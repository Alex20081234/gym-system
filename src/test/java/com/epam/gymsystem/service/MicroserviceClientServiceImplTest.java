package com.epam.gymsystem.service;

import com.epam.gymsystem.dto.ActionType;
import com.epam.gymsystem.dto.SubmitWorkloadChangesRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jms.core.JmsTemplate;
import static org.mockito.Mockito.*;

class MicroserviceClientServiceImplTest {
    private static final String QUEUE_NAME = "test-queue";
    private JmsTemplate jmsTemplate;
    private MessageSenderServiceImpl service;

    @BeforeEach
    void setUp() {
        jmsTemplate = mock(JmsTemplate.class);
        service = new MessageSenderServiceImpl(jmsTemplate, QUEUE_NAME);
    }

    @Test
    void sendMessageShouldTryToSendMessageToQueue() {
        SubmitWorkloadChangesRequestBody body = SubmitWorkloadChangesRequestBody.builder()
                .trainerUsername("John.Doe")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .trainerIsActive(true)
                .changeType(ActionType.ADD)
                .trainingDate("2024-10-12")
                .trainingDurationMinutes(10)
                .build();
        service.sendMessage(body);
        verify(jmsTemplate, times(1)).convertAndSend(QUEUE_NAME, body);
    }
}
