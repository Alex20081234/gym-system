package com.epam.gymsystem.controller;

import com.epam.gymsystem.common.MappingUtils;
import com.epam.gymsystem.domain.*;
import com.epam.gymsystem.dto.ResponseTraining;
import com.epam.gymsystem.service.TrainingService;
import com.epam.gymsystem.dto.RequestTraining;
import com.epam.gymsystem.dto.ShortTrainingType;
import com.epam.gymsystem.service.TraineeService;
import com.epam.gymsystem.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.time.LocalDate;
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
    @PostMapping("/{username}")
    public ResponseEntity<Void> addTraining(@PathVariable String username,
                                            @RequestBody RequestTraining requestTraining) {
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
    @GetMapping("/types/{username}")
    public ResponseEntity<List<ShortTrainingType>> getTrainingTypes(@PathVariable String username) {
        List<TrainingType> types = trainingService.selectAllTypes();
        return ResponseEntity.ok(types.stream().map(MappingUtils::fromTrainingTypeToShortTrainingType).toList());
    }

    @Operation(summary = "Get trainings for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTraining.class))),
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
    @GetMapping("/{username}")
    public ResponseEntity<List<ResponseTraining>> getTrainings(@PathVariable String username,
                                                               @RequestParam(required = false) String startDate,
                                                               @RequestParam(required = false) String endDate,
                                                               @RequestParam(required = false) String partnerName,
                                                               @RequestParam(required = false) String typeName) {
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(startDate == null ? null : LocalDate.parse(startDate))
                .toDate(endDate == null ? null : LocalDate.parse(endDate))
                .partnerUsername(partnerName)
                .trainingType(typeName == null ? null : trainingService.selectType(typeName))
                .build();
        List<Training> trainings = trainingService.selectTrainings(username, criteria);
        return ResponseEntity.ok(trainings.stream()
                .map(training -> MappingUtils.fromTrainingToResponseTraining(training, username)).toList());
    }
}
