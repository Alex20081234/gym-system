package com.epam.task.gymsystem.controller;

import com.epam.task.gymsystem.common.MappingUtils;
import com.epam.task.gymsystem.domain.*;
import com.epam.task.gymsystem.dto.*;
import com.epam.task.gymsystem.security.AuthTokenUtil;
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
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trainers")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public TrainerController(TrainerService trainerService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Operation(summary = "Register a new trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer registered successfully",
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
    public ResponseEntity<UsernameAndPassword> registerTrainer(@RequestBody RequestTrainer requestTrainer) {
        Trainer trainer = MappingUtils.fromRequestTrainerToTrainer(requestTrainer);
        UsernameAndPassword usernameAndPassword = trainerService.create(trainer);
        return ResponseEntity.created(URI.create("/trainer/get/" + usernameAndPassword.getUsername()))
                .body(usernameAndPassword);
    }

    @Operation(summary = "Change trainer's login")
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
    public ResponseEntity<Void> changeLogin(@PathVariable String username,
                                            @RequestBody Passwords passwords,
                                            HttpServletRequest request) throws AccessDeniedException {
        AuthTokenUtil.verifyCookie(request, username, passwords.getOldPassword());
        Trainer trainer = trainerService.select(username);
        if (trainer.getPassword().equals(passwords.getOldPassword())) {
            trainerService.changePassword(username, passwords.getNewPassword());
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get trainer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer details retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTrainer.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainer not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @GetMapping("/{username}")
    public ResponseEntity<ResponseTrainer> getTrainer(@PathVariable String username,
                                                      HttpServletRequest request) throws AccessDeniedException {
        Trainer trainer = trainerService.select(username);
        AuthTokenUtil.verifyCookie(request, username, trainer.getPassword());
        return ResponseEntity.ok(MappingUtils.fromTrainerToResponseTrainer(trainer));
    }

    @Operation(summary = "Update trainer profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTrainerWithUsername.class))),
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
    public ResponseEntity<ResponseTrainerWithUsername> updateTrainer(@PathVariable String username,
                                                                     @RequestBody ExtendedRequestTrainer extendedRequestTrainer,
                                                                     HttpServletRequest request) throws AccessDeniedException {
        String password = trainerService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        Trainer trainer = MappingUtils.fromExtendedRequestTrainerToTrainer(extendedRequestTrainer);
        String newUsername = trainerService.update(username, trainer);
        Trainer updatedTrainer = trainerService.select(newUsername);
        return ResponseEntity.ok(MappingUtils.fromTrainerToResponseTrainerWithUsername(updatedTrainer));
    }

    @Operation(summary = "Get trainings for a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTraining.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainer not found"))),
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
        String password = trainerService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        TrainingCriteria criteria = TrainingCriteria.builder()
                .fromDate(startDate == null ? null : LocalDate.parse(startDate))
                .toDate(endDate == null ? null : LocalDate.parse(endDate))
                .partnerUsername(partnerName)
                .trainingType(typeName == null ? null : trainingService.selectType(typeName))
                .build();
        List<Training> trainings = trainerService.selectTrainings(username, criteria);
        return ResponseEntity.ok(trainings.stream()
                .map(training -> MappingUtils.fromTrainingToResponseTraining(training, username)).toList());
    }

    @Operation(summary = "Change activity status of a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity status changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unauthorized access"))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Trainer not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @PatchMapping("/{username}/activity-status")
    public ResponseEntity<Void> changeActivityStatus(@PathVariable String username, @RequestParam Boolean isActive,
                                                     HttpServletRequest request) throws AccessDeniedException {
        String password = trainerService.select(username).getPassword();
        AuthTokenUtil.verifyCookie(request, username, password);
        trainerService.changeActivityStatus(username, isActive);
        return ResponseEntity.ok().build();
    }
}