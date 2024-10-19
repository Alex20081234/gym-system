package com.epam.gymsystem.controller;

import com.epam.gymsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void loginShouldReturnStatusOkWhenValidCredentials() throws Exception {
        when(authService.selectPassword("validUser")).thenReturn("validPassword");
        mockMvc.perform(get("/login")
                        .param("username", "validUser")
                        .param("password", "validPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void loginShouldReturnStatusUnauthorizedWhenInvalidCredentials() throws Exception {
        when(authService.selectPassword("validUser")).thenReturn("wrongPassword");
        mockMvc.perform(get("/login")
                        .param("username", "validUser")
                        .param("password", "validPassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
