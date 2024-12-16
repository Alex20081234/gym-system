package com.epam.gymsystem.controller;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.dto.Passwords;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @Mock
    private TraineeController traineeController;

    @Mock
    private TrainingController trainingController;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController, trainingController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void handleUserNotFoundExceptionShouldProcessUserNotFoundException() throws Exception {
        when(traineeController.getTrainee(anyString())).thenThrow(new UserNotFoundException("Trainee with username Non.Existent was not found"));
        mockMvc.perform(get("/api/v1/trainees/Non.Existent"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Trainee with username Non.Existent was not found"));
    }

    @Test
    void handleIllegalArgumentExceptionShouldProcessIllegalArgumentException() throws Exception {
        Passwords passwords = Passwords.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(passwords);
        when(traineeController.changeLogin(any(), any())).thenThrow(new IllegalArgumentException("Invalid password"));
        mockMvc.perform(put("/api/v1/trainees/login/Test.User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid password"));
    }

    @Test
    void handleExceptionShouldProcessAnyOtherException() throws Exception {
        when(traineeController.getTrainee(anyString())).thenThrow(new RuntimeException("General exception"));
        mockMvc.perform(get("/api/v1/trainees/Test.User"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error occurred : General exception"));
    }
}
