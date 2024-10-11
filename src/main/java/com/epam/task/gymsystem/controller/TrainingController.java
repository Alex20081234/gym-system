package com.epam.task.gymsystem.controller;

import com.epam.task.gymsystem.common.MappingUtils;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.*;
import com.epam.task.gymsystem.dto.RequestTraining;
import com.epam.task.gymsystem.dto.ShortTrainingType;
import com.epam.task.gymsystem.security.AuthTokenUtil;
import com.epam.task.gymsystem.service.TraineeService;
import com.epam.task.gymsystem.service.TrainerService;
import com.epam.task.gymsystem.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    @Autowired
    public TrainingController(TrainingService trainingService, TrainerService trainerService, TraineeService traineeService) {
        this.trainingService = trainingService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    @Operation(summary = "Add a new training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training added successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "User not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PostMapping("/{username}/training")
    public ResponseEntity<Void> addTraining(@PathVariable String username,
                                            @RequestBody RequestTraining requestTraining,
                                            HttpServletRequest request) throws AccessDeniedException {
        String password;
        try {
            password = trainerService.select(username).getPassword();
        } catch (UserNotFoundException e) {
            try {
                password = traineeService.select(username).getPassword();
            } catch (UserNotFoundException e1) {
                throw new AccessDeniedException("Unauthorized access");
            }
        }
        AuthTokenUtil.verifyCookie(request, username, password);
        Trainer trainer = trainerService.select(requestTraining.getTrainerUsername());
        Trainee trainee = traineeService.select(requestTraining.getTraineeUsername());
        Training training = MappingUtils.fromRequestTrainingToTraining(requestTraining);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainer.getSpecialization());
        trainingService.create(training);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training types retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortTrainingType.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "User not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @GetMapping("/{username}/types")
    public ResponseEntity<List<ShortTrainingType>> getTrainingTypes(@PathVariable String username,
                                                                    HttpServletRequest request) throws AccessDeniedException {
        String password;
        try {
            password = trainerService.select(username).getPassword();
        } catch (UserNotFoundException e) {
            try {
                password = traineeService.select(username).getPassword();
            } catch (UserNotFoundException e1) {
                throw new AccessDeniedException("Unauthorized access");
            }
        }
        AuthTokenUtil.verifyCookie(request, username, password);
        List<TrainingType> types = trainingService.selectAllTypes();
        return ResponseEntity.ok(types.stream().map(MappingUtils::fromTrainingTypeToShortTrainingType).toList());
    }
}
