package com.epam.gymsystem.controller;

import com.epam.gymsystem.service.AuthService;
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
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
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
            @Parameter(description = "Password of the user", required = true) @RequestParam String password) {
        String foundPassword = authService.selectPassword(username);
        if (!foundPassword.equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful");
    }
}
