package com.epam.task.gymsystem.controller;

import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.User;
import com.epam.task.gymsystem.security.AuthTokenUtil;
import com.epam.task.gymsystem.service.TraineeService;
import com.epam.task.gymsystem.service.TrainerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
public class AuthController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public AuthController(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Operation(summary = "User login", description = "Logs in a trainee or trainer based on the provided username and password. Returns a token upon successful login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login, returns authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Invalid username or password"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "User not found"))),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "Unexpected error occurred: <error details>")))
    })
    @GetMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "Username of the user", required = true) @RequestParam String username,
            @Parameter(description = "Password of the user", required = true) @RequestParam String password,
            HttpServletResponse response) {

        User user;
        try {
            user = traineeService.select(username);
        } catch (UserNotFoundException e) {
            try {
                user = trainerService.select(username);
            } catch (UserNotFoundException e1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        }

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = AuthTokenUtil.generateToken(username, password);
        Cookie cookie = new Cookie("authCookie", token);
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/gym-system");
        response.addCookie(cookie);
        return ResponseEntity.ok(token);
    }
}
