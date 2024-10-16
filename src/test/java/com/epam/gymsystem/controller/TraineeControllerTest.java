package com.epam.gymsystem.controller;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dto.*;
import com.epam.gymsystem.service.AuthServiceImpl;
import com.epam.gymsystem.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TraineeControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @Mock
    private AuthServiceImpl authService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
                .setControllerAdvice(handler)
                .build();
    }

    @Test
    void registerTraineeShouldTryToRegisterTrainee() throws Exception {
        RequestTrainee requestTrainee = RequestTrainee.builder()
                .firstName("Test")
                .lastName("User")
                .dateOfBirth("1988-10-12")
                .address("New Lane Avenue")
                .build();
        UsernameAndPassword expectedResponse = UsernameAndPassword.builder()
                .username("Test.User")
                .password("password")
                .build();
        when(traineeService.create(any())).thenReturn(expectedResponse);
        mockMvc.perform(post("/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestTrainee)))
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
        mockMvc.perform(put("/trainees/login/Test.User")
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
        mockMvc.perform(put("/trainees/login/Test.User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwords)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect password"));
    }

    @Test
    void getTraineeShouldTryToSelectTrainee() throws Exception {
        Trainee trainee = Trainee.builder()
                .firstName("Test")
                .lastName("User")
                .username("Test.User")
                .password("password")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1989, 10, 12))
                .address("New Lane Avenue")
                .build();
        when(traineeService.select("Test.User")).thenReturn(trainee);
        mockMvc.perform(get("/trainees/Test.User"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Test\",\"lastName\":\"User\"" +
                        ",\"dateOfBirth\":\"1989-10-12\",\"address\":\"New Lane Avenue\"" +
                        ",\"isActive\":true,\"trainers\":[]}"));
    }

    @Test
    void updateTraineeShouldTryToUpdateTrainee() throws Exception {
        ExtendedRequestTrainee requestTrainee = ExtendedRequestTrainee.builder()
                .firstName("Updated")
                .lastName("User")
                .dateOfBirth("1988-10-12")
                .address("New Lane Avenue")
                .isActive("true")
                .build();
        Trainee updated = Trainee.builder()
                .firstName("Updated")
                .lastName("User")
                .username("Updated.User")
                .password("password")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1988, 10, 12))
                .address("New Lane Avenue")
                .build();
        when(traineeService.update(eq("Test.User"), any())).thenReturn("Updated.User");
        when(traineeService.select("Updated.User")).thenReturn(updated);
        mockMvc.perform(put("/trainees/profile/Test.User")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestTrainee)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("{\"firstName\":\"Updated\",\"lastName\":\"User\"" +
                                ",\"dateOfBirth\":\"1988-10-12\",\"address\":\"New Lane Avenue\"" +
                                ",\"isActive\":true}"));
    }

    @Test
    void deleteTraineeShouldTryToDeleteTrainee() throws Exception {
        doNothing().when(traineeService).delete("Test.User");
        mockMvc.perform(delete("/trainees/Test.User"))
                .andExpect(status().isOk());
    }

    @Test
    void selectNotAssignedTrainersShouldTryToFindNotAssignedTrainers() throws Exception {
        Trainer first = Trainer.builder()
                .firstName("First")
                .lastName("Trainer")
                .username("First.Trainer")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder()
                        .id(100)
                        .name("Boxing")
                        .build())
                .build();
        Trainer second = Trainer.builder()
                .firstName("Second")
                .lastName("Trainer")
                .username("Second.Trainer")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder()
                        .id(100)
                        .name("Boxing")
                        .build())
                .build();
        when(traineeService.selectNotAssignedTrainers("Test.User"))
                .thenReturn(List.of(first, second));
        mockMvc.perform(get("/trainees/not-assigned-trainers/Test.User"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"First\",\"lastName\":\"Trainer\"," +
                        "\"specialization\":{\"name\":\"Boxing\",\"id\":100},\"username\":\"First.Trainer\"}" +
                        ",{\"firstName\":\"Second\",\"lastName\":\"Trainer\"," +
                        "\"specialization\":{\"name\":\"Boxing\",\"id\":100},\"username\":\"Second.Trainer\"}]"));
    }

    @Test
    void updateTrainersShouldTryToUpdateTrainers() throws Exception {
        TrainerRequiredPair pair = TrainerRequiredPair.builder()
                .username("Test.Trainer")
                .required(true)
                .build();
        Trainer trainer = Trainer.builder()
                .firstName("Test")
                .lastName("Trainer")
                .username("Test.Trainer")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder()
                        .id(100)
                        .name("Boxing")
                        .build())
                .build();
        Trainee trainee = Trainee.builder()
                .firstName("Test")
                .lastName("User")
                .username("Test.User")
                .password("password")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1988, 10, 12))
                .address("New Lane Avenue")
                .trainers(Set.of(trainer))
                .build();
        doNothing().when(traineeService).updateTrainers(anyString(), any());
        when(traineeService.select("Test.User")).thenReturn(trainee);
        doNothing().when(traineeService).loadDependencies(any());
        mockMvc.perform(put("/trainees/Test.User/trainers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(List.of(pair))))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"Test\",\"lastName\":\"Trainer\"," +
                        "\"specialization\":{\"name\":\"Boxing\",\"id\":100},\"username\":\"Test.Trainer\"}]"));
    }

    @Test
    void changeActivityStatusShouldTryToChangeStatus() throws Exception {
        doNothing().when(traineeService).changeActivityStatus("Test.User", false);
        mockMvc.perform(patch("/trainees/activity-status/Test.User?isActive=false"))
                .andExpect(status().isOk());
    }
}
