package com.epam.gymsystem.controller;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.dto.*;
import com.epam.gymsystem.service.AuthService;
import com.epam.gymsystem.common.MappingUtils;
import com.epam.gymsystem.service.MessageSenderService;
import com.epam.gymsystem.service.TraineeService;
import com.epam.gymsystem.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trainees")
@AllArgsConstructor
public class TraineeController {
    private static final String NOT_FOUND = "Trainee with username %s was not found";
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final AuthService authService;
    private final MessageSenderService messageSenderService;

    @Operation(summary = "Register a new trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credentials.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Invalid input data"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PostMapping
    public ResponseEntity<Credentials> registerTrainee(@RequestBody RequestTrainee requestTrainee) {
        Trainee trainee = MappingUtils.fromRequestTraineeToTrainee(requestTrainee);
        Credentials usernameAndPassword = traineeService.create(trainee);
        return ResponseEntity.created(URI.create("/trainees/" + usernameAndPassword.getUsername()))
                .body(usernameAndPassword);
    }

    @Operation(summary = "Change trainee's login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Login changed successfully"),
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
    @Secured("ROLE_USER")
    @PutMapping("/login/{username}")
    public ResponseEntity<Void> changeLogin(@PathVariable String username, @RequestBody Passwords passwords) {
        String foundPassword = authService.selectPassword(username);
        if (foundPassword.equals(passwords.getOldPassword())) {
            traineeService.changePassword(username, passwords.getNewPassword());
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
        return ResponseEntity.noContent().build();
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
    @Secured("ROLE_USER")
    @GetMapping("/{username}")
    public ResponseEntity<ResponseTrainee> getTrainee(@PathVariable String username) {
        Trainee trainee = traineeService.select(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
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
    @Secured("ROLE_USER")
    @PutMapping("/profile/{username}")
    public ResponseEntity<ResponseTraineeWithUsername> updateTrainee(@PathVariable String username,
                                                                     @RequestBody ExtendedRequestTrainee extendedRequestTrainee) {
        Trainee trainee = MappingUtils.fromExtendedRequestTraineeToTrainee(extendedRequestTrainee);
        String newUsername = traineeService.update(username, trainee);
        Trainee updatedTrainee = traineeService.select(newUsername)
                .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, newUsername)));
        return ResponseEntity.ok(MappingUtils.fromTraineeToResponseTraineeWithUsername(updatedTrainee));
    }

    @Operation(summary = "Delete trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
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
    @Secured("ROLE_USER")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String username) {
        List<Training> trainings = trainingService.selectTrainings(username, null);
        trainings.forEach(t -> {
            SubmitWorkloadChangesRequestBody requestBody = SubmitWorkloadChangesRequestBody.builder()
                    .trainerUsername(t.getTrainer().getUsername())
                    .trainerFirstName(t.getTrainer().getFirstName())
                    .trainerLastName(t.getTrainer().getLastName())
                    .trainerIsActive(t.getTrainer().getIsActive())
                    .trainingDate(t.getTrainingDate().toString())
                    .trainingDurationMinutes(t.getDuration())
                    .changeType(ActionType.DELETE)
                    .build();
            messageSenderService.sendMessage(requestBody);
        });
        traineeService.delete(username);
        return ResponseEntity.noContent().build();
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
    @Secured("ROLE_USER")
    @GetMapping("/not-assigned-trainers/{username}")
    public ResponseEntity<List<ShortTrainer>> getNotAssignedTrainers(@PathVariable String username) {
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
    @Secured("ROLE_USER")
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<ShortTrainer>> updateTrainers(@PathVariable String username,
                                                             @RequestBody List<TrainerRequiredPair> pairs) {
        Map<String, Boolean> updates = new HashMap<>();
        pairs.forEach(pair -> updates.put(pair.getUsername(), pair.getRequired()));
        traineeService.updateTrainers(username, updates);
        Trainee trainee = traineeService.select(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        traineeService.loadDependencies(trainee);
        if (!trainee.getTrainers().isEmpty()) {
            return ResponseEntity.ok(trainee.getTrainers().stream().map(MappingUtils::fromTrainerToShortTrainer).toList());
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Operation(summary = "Change activity status of a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity status changed successfully"),
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
    @Secured("ROLE_USER")
    @PatchMapping("/activity-status/{username}")
    public ResponseEntity<Void> changeActivityStatus(@PathVariable String username, @RequestParam Boolean isActive) {
        traineeService.changeActivityStatus(username, isActive);
        return ResponseEntity.noContent().build();
    }
}
