package com.epam.gymsystem.controller;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dto.RequestTraining;
import com.epam.gymsystem.service.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrainingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private MockMvc mockMvc;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private TraineeServiceImpl traineeService;

    @Mock
    private TrainingServiceImpl trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController)
                .setControllerAdvice(handler)
                .build();
    }

    @Test
    void addTrainingShouldTryToAddTraining() throws Exception {
        RequestTraining training = RequestTraining.builder()
                .traineeUsername("Test.Trainee")
                .trainerUsername("Test.Trainer")
                .name("Boxing practise")
                .date("2024-10-17")
                .duration(60)
                .build();
        Trainee trainee = Trainee.builder()
                .firstName("Test")
                .lastName("Trainee")
                .username("Test.Trainee")
                .password("password")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1989, 10, 12))
                .address("New Lane Avenue")
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
        when(trainerService.select("Test.Trainer")).thenReturn(trainer);
        when(traineeService.select("Test.Trainee")).thenReturn(trainee);
        doNothing().when(trainingService).create(any());
        mockMvc.perform(post("/trainings/Test.Trainee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(training)))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainingTypesShouldTryToSelectTrainingTypes() throws Exception {
        TrainingType type = TrainingType.builder()
                .name("Boxing")
                .id(100)
                .build();
        when(trainingService.selectAllTypes()).thenReturn(List.of(type));
        mockMvc.perform(get("/trainings/types/Test.User"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Boxing\",\"id\":100}]"));
    }

    @Test
    void getTrainingsShouldTryToReturnTrainings()  throws Exception {
        Trainee trainee = Trainee.builder()
                .firstName("Test")
                .lastName("Trainee")
                .username("Test.Trainee")
                .password("password")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1989, 10, 12))
                .address("New Lane Avenue")
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
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("Boxing practise")
                .trainingType(TrainingType.builder()
                        .name("Boxing")
                        .id(100)
                        .build())
                .trainingDate(LocalDate.of(2024, 10, 17))
                .duration(60)
                .build();
        when(trainingService.selectTrainings(any(), any())).thenReturn(List.of(training));
        mockMvc.perform(get("/trainings/Test.Trainee"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Boxing practise\",\"date\":\"2024-10-17\"," +
                        "\"type\":{\"name\":\"Boxing\",\"id\":100},\"duration\":60,\"partnerName\":\"Test.Trainer\"}]"));
    }
}
