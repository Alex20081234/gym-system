package com.epam.task.gymsystem.controller;

import com.epam.task.gymsystem.common.MappingUtils;
import com.epam.task.gymsystem.dto.*;
import com.epam.task.gymsystem.domain.*;
import com.epam.task.gymsystem.security.AuthTokenUtil;
import com.epam.task.gymsystem.service.TraineeService;
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
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainees")
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public TraineeController(TraineeService traineeService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    @Operation(summary = "Register a new trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsernameAndPassword.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Invalid input data"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PostMapping("/register")
    public ResponseEntity<UsernameAndPassword> registerTrainee(@RequestBody RequestTrainee requestTrainee) {
        Trainee trainee = MappingUtils.fromRequestTraineeToTrainee(requestTrainee);
        UsernameAndPassword usernameAndPassword = traineeService.create(trainee);
        return ResponseEntity.created(URI.create("/trainee/get/" + usernameAndPassword.getUsername()))
                .body(usernameAndPassword);
    }

    @Operation(summary = "Change trainee's login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "400", description = "Incorrect password",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Incorrect password"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PutMapping("/{username}/login")
    public ResponseEntity<Void> changeLogin(@PathVariable String username, @RequestBody Passwords passwords,
                                            HttpServletRequest request) throws AccessDeniedException {
        AuthTokenUtil.verifyCookie(request, username, passwords.getOldPassword());
        Trainee trainee = traineeService.select(username);
        if (trainee.getPassword().equals(passwords.getOldPassword())) {
            traineeService.changePassword(username, passwords.getNewPassword());
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get trainee details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee details retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTrainee.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainee not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @GetMapping("/{username}")
    public ResponseEntity<ResponseTrainee> getTrainee(@PathVariable String username,
                                                      HttpServletRequest request) throws AccessDeniedException {
        Trainee trainee = traineeService.select(username);
        AuthTokenUtil.verifyCookie(request, username, trainee.getPassword());
        return ResponseEntity.ok(MappingUtils.fromTraineeToResponseTrainee(trainee));
    }

    @Operation(summary = "Update trainee profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTraineeWithUsername.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Invalid input data"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PutMapping("/{username}/profile")
    public ResponseEntity<ResponseTraineeWithUsername> updateTrainee(@PathVariable String username,
                                                                     @RequestBody ExtendedRequestTrainee extendedRequestTrainee,
                                                                     HttpServletRequest request) throws AccessDeniedException {
        String password = traineeService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        Trainee trainee = MappingUtils.fromExtendedRequestTraineeToTrainee(extendedRequestTrainee);
        String newUsername = traineeService.update(username, trainee);
        Trainee updatedTrainee = traineeService.select(newUsername);
        return ResponseEntity.ok(MappingUtils.fromTraineeToResponseTraineeWithUsername(updatedTrainee));
    }

    @Operation(summary = "Delete trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainee not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @DeleteMapping("/{username}/delete")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String username,
                                              HttpServletRequest request) throws AccessDeniedException {
        String password = traineeService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        traineeService.delete(username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get not assigned trainers for a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Not assigned trainers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortTrainer.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainee not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @GetMapping("/{username}/not-assigned-trainers")
    public ResponseEntity<List<ShortTrainer>> getNotAssignedTrainers(@PathVariable String username,
                                                                     HttpServletRequest request) throws AccessDeniedException {
        String password = traineeService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        List<Trainer> trainers = traineeService.selectNotAssignedTrainers(username);
        return ResponseEntity.ok(trainers.stream().map(MappingUtils::fromTrainerToShortTrainer).toList());
    }

    @Operation(summary = "Update trainers for a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortTrainer.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainee not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<ShortTrainer>> updateTrainers(@PathVariable String username,
                                                             @RequestBody List<TrainerRequiredPair> pairs,
                                                             HttpServletRequest request) throws AccessDeniedException {
        String password = traineeService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        Map<String, Boolean> updates = new HashMap<>();
        pairs.forEach(pair -> updates.put(pair.getUsername(), pair.getRequired()));
        traineeService.updateTrainers(username, updates);
        Trainee trainee = traineeService.select(username);
        traineeService.loadDependencies(trainee);
        return ResponseEntity.ok(trainee.getTrainers().stream().map(MappingUtils::fromTrainerToShortTrainer).toList());
    }

    @Operation(summary = "Get trainings for a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTraining.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainee not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<ResponseTraining>> getTrainings(@PathVariable String username,
                                                               @RequestParam(required = false) String startDate,
                                                               @RequestParam(required = false) String endDate,
                                                               @RequestParam(required = false) String partnerName,
                                                               @RequestParam(required = false) String typeName,
                                                               HttpServletRequest request) throws AccessDeniedException {
        String password = traineeService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(startDate == null ? null : LocalDate.parse(startDate))
                .toDate(endDate == null ? null : LocalDate.parse(endDate))
                .partnerUsername(partnerName)
                .trainingType(typeName == null ? null : trainingService.selectType(typeName))
                .build();
        List<Training> trainings = traineeService.selectTrainings(username, criteria);
        return ResponseEntity.ok(trainings.stream()
                .map(training -> MappingUtils.fromTrainingToResponseTraining(training, username)).toList());
    }

    @Operation(summary = "Change activity status of a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity status changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainee not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PatchMapping("/{username}/activity-status")
    public ResponseEntity<Void> changeActivityStatus(@PathVariable String username, @RequestParam Boolean isActive,
                                                     HttpServletRequest request) throws AccessDeniedException {
        String password = traineeService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        traineeService.changeActivityStatus(username, isActive);
        return ResponseEntity.ok().build();
    }
}
