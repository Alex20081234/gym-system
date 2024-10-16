package com.epam.gymsystem.controller;

import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dto.*;
import com.epam.gymsystem.service.AuthServiceImpl;
import com.epam.gymsystem.service.TrainerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private MockMvc mockMvc;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private AuthServiceImpl authService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
                .setControllerAdvice(handler)
                .build();
    }

    @Test
    void registerTrainerShouldTryToRegisterTrainer() throws Exception {
        RequestTrainer requestTrainer = RequestTrainer.builder()
                .firstName("Test")
                .lastName("User")
                .specialization(ShortTrainingType.builder()
                        .name("Boxing")
                        .id(100)
                        .build())
                .build();
        UsernameAndPassword expectedResponse = UsernameAndPassword.builder()
                .username("Test.User")
                .password("password")
                .build();
        when(trainerService.create(any())).thenReturn(expectedResponse);
        mockMvc.perform(post("/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestTrainer)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"username\":\"Test.User\", \"password\":\"password\"}"));
    }

    @Test
    void changeLoginShouldTryToUpdateLogin() throws Exception {
        Passwords passwords = Passwords.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();
        when(authService.selectPassword("Test.User")).thenReturn("oldPassword");
        mockMvc.perform(put("/trainers/login/Test.User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwords)))
                .andExpect(status().isOk());
    }

    @Test
    void changeLoginShouldReturnBadRequestWhenPasswordIncorrect() throws Exception {
        Passwords passwords = Passwords.builder()
                .oldPassword("invalidPassword")
                .newPassword("newPassword")
                .build();
        when(authService.selectPassword("Test.User")).thenReturn("oldPassword");
        mockMvc.perform(put("/trainers/login/Test.User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwords)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect password"));
    }

    @Test
    void getTrainerShouldTryToSelectTrainer() throws Exception {
        Trainer trainer = Trainer.builder()
                .firstName("Test")
                .lastName("User")
                .username("Test.User")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder()
                        .name("Boxing")
                        .id(100)
                        .build())
                .build();
        when(trainerService.select("Test.User")).thenReturn(trainer);
        mockMvc.perform(get("/trainers/Test.User"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Test\",\"lastName\":\"User\"" +
                        ",\"specialization\":{\"name\":\"Boxing\",\"id\":100}" +
                        ",\"isActive\":true,\"trainees\":[]}"));
    }

    @Test
    void updateTrainerShouldTryToUpdateTrainer() throws Exception {
        ExtendedRequestTrainer requestTrainer = ExtendedRequestTrainer.builder()
                .firstName("Updated")
                .lastName("User")
                .specialization(ShortTrainingType.builder()
                        .name("Boxing")
                        .id(100)
                        .build())
                .isActive("true")
                .build();
        Trainer updated = Trainer.builder()
                .firstName("Updated")
                .lastName("User")
                .username("Updated.User")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder()
                        .name("Boxing")
                        .id(100)
                        .build())
                .build();
        when(trainerService.update(eq("Test.User"), any())).thenReturn("Updated.User");
        when(trainerService.select("Updated.User")).thenReturn(updated);
        mockMvc.perform(put("/trainers/profile/Test.User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestTrainer)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("{\"firstName\":\"Updated\"," +
                                "\"lastName\":\"User\"," +
                                "\"specialization\":{\"name\":\"Boxing\",\"id\":100}," +
                                "\"isActive\":true}"));
    }

    @Test
    void changeActivityStatusShouldTryToChangeStatus() throws Exception {
        doNothing().when(trainerService).changeActivityStatus("Test.User", false);
        mockMvc.perform(patch("/trainers/activity-status/Test.User?isActive=false"))
                .andExpect(status().isOk());
    }
}
