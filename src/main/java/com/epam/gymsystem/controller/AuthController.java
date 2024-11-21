package com.epam.gymsystem.controller;

import com.epam.gymsystem.dao.UserTriesDao;
import com.epam.gymsystem.security.JwtService;
import com.epam.gymsystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final UserTriesDao dao;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @Value("${gym-system.maxFailedAttempts}")
    private int maxFailedAttempts;

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
        if (dao.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You have been blocked for 5 minutes");
        }
        String foundPassword = authService.selectPassword(username);
        if (!encoder.matches(password, foundPassword)) {
            dao.incrementAttemptsOrCreateEntry(username);
            if (dao.attempts(username) == maxFailedAttempts) {
                dao.block(username);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        String token = authenticate(username, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/logout")
    @Operation(summary = "Logout User",
            description = "Logs out the user by blacklisting the provided JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful logout. The token has been blacklisted."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request is malformed."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The provided token is invalid or expired."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error. An error occurred while processing the request.")
    })
    public ResponseEntity<Void> logout(
            @Parameter(description = "HTTP request object containing the user's JWT token.")
            HttpServletRequest request) {
        String token = jwtService.parseJwt(request);
        jwtService.blacklistToken(token);
        return ResponseEntity.noContent().build();
    }

    private String authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateJwtToken(authentication);
    }
}
