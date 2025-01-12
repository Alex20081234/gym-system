package com.epam.gymsystem.controller;

import com.epam.gymsystem.common.MappingUtils;
import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.dto.*;
import com.epam.gymsystem.service.AuthService;
import com.epam.gymsystem.service.TrainerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/trainers")
@AllArgsConstructor
public class TrainerController {
    private static final String NOT_FOUND = "Trainer with username %s was not found";
    private final TrainerService trainerService;
    private final PasswordEncoder encoder;
    private final AuthService authService;

    @Operation(summary = "Register a new trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer registered successfully",
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
    public ResponseEntity<Credentials> registerTrainer(@RequestBody RequestTrainer requestTrainer) {
        Trainer trainer = MappingUtils.fromRequestTrainerToTrainer(requestTrainer);
        Credentials usernameAndPassword = trainerService.create(trainer);
        return ResponseEntity.created(URI.create("/trainers/" + usernameAndPassword.getUsername()))
                .body(usernameAndPassword);
    }

    @Operation(summary = "Change trainer's login")
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
    public ResponseEntity<Void> changeLogin(@PathVariable String username,
                                            @RequestBody Passwords passwords) {
        String foundPassword = authService.selectPassword(username);
        if (encoder.matches(passwords.getOldPassword(), foundPassword)) {
            trainerService.changePassword(username, passwords.getNewPassword());
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
        return ResponseEntity.noContent().build();
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
    @Secured("ROLE_USER")
    @GetMapping("/{username}")
    public ResponseEntity<ResponseTrainer> getTrainer(@PathVariable String username) {
        Trainer trainer = trainerService.select(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
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
    @Secured("ROLE_USER")
    @PutMapping("/profile/{username}")
    public ResponseEntity<ResponseTrainerWithUsername> updateTrainer(@PathVariable String username,
                                                                     @RequestBody ExtendedRequestTrainer extendedRequestTrainer) {
        Trainer trainer = MappingUtils.fromExtendedRequestTrainerToTrainer(extendedRequestTrainer);
        String newUsername = trainerService.update(username, trainer);
        Trainer updatedTrainer = trainerService.select(newUsername)
                .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, newUsername)));
        return ResponseEntity.ok(MappingUtils.fromTrainerToResponseTrainerWithUsername(updatedTrainer));
    }

    @Operation(summary = "Change activity status of a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity status changed successfully"),
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
    @Secured("ROLE_USER")
    @PatchMapping("/activity-status/{username}")
    public ResponseEntity<Void> changeActivityStatus(@PathVariable String username, @RequestParam Boolean isActive) {
        trainerService.changeActivityStatus(username, isActive);
        return ResponseEntity.noContent().build();
    }
}
