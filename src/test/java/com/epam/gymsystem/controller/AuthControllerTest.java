package com.epam.gymsystem.controller;

import com.epam.gymsystem.security.JwtService;
import com.epam.gymsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private AuthenticationManager manager;
    @Mock
    private JwtService service;
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
        when(encoder.matches(any(), anyString())).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        when(manager.authenticate(any())).thenReturn(authentication);
        when(service.generateJwtToken(any())).thenReturn("abcd");
        mockMvc.perform(get("/login")
                        .param("username", "validUser")
                        .param("password", "validPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("abcd"));
    }

    @Test
    void loginShouldReturnUnauthorizedWhenIncorrectCredentials() throws Exception {
        String expected = "Invalid username or password";
        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                expected = "You have been blocked for 5 minutes";
            }
            when(authService.selectPassword("validUser")).thenReturn("invalidPassword");
            when(encoder.matches(any(), anyString())).thenReturn(false);
            mockMvc.perform(get("/login")
                            .param("username", "validUser")
                            .param("password", "validPassword"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(expected));
        }
    }

    @Test
    void logoutShouldReturnNoContentAndInvalidateToken() throws Exception {
        when(service.parseJwt(any())).thenReturn("validtoken");
        doNothing().when(service).blacklistToken("validtoken");
        mockMvc.perform(get("/logout")
                    .header("Authorization", "Bearer " + "validtoken"))
                .andExpect(status().isNoContent());
        verify(service, times(1)).blacklistToken("validtoken");
    }
}
