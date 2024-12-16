package com.epam.gymsystem.controller;

import com.epam.gymsystem.dao.UserTriesDao;
import com.epam.gymsystem.security.JwtService;
import com.epam.gymsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
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
    @Mock
    private UserTriesDao dao;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authController, "maxFailedAttempts", 3);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void loginShouldReturnStatusOkWhenValidCredentials() throws Exception {
        when(dao.isBlocked(anyString())).thenReturn(false);
        when(authService.selectPassword("validUser")).thenReturn("validPassword");
        when(encoder.matches(any(), anyString())).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        when(manager.authenticate(any())).thenReturn(authentication);
        when(service.generateJwtToken(any())).thenReturn("abcd");
        mockMvc.perform(get("/api/v1/login")
                        .param("username", "validUser")
                        .param("password", "validPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("abcd"));
    }

    @Test
    void loginShouldReturnUnauthorizedWhenIncorrectCredentials() throws Exception {
        String expected = "Invalid username or password";
        for (int i = 1; i < 5; i++) {
            if (i == 4) {
                expected = "You have been blocked for 5 minutes";
                when(dao.isBlocked(anyString())).thenReturn(true);
            } else {
                when(dao.isBlocked(anyString())).thenReturn(false);
                when(authService.selectPassword("validUser")).thenReturn("invalidPassword");
                when(encoder.matches(any(), anyString())).thenReturn(false);
                doNothing().when(dao).incrementAttemptsOrCreateEntry(anyString());
                when(dao.attempts(anyString())).thenReturn(i);
                if (i == 3) {
                    doNothing().when(dao).block(anyString());
                }
            }
            mockMvc.perform(get("/api/v1/login")
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
        mockMvc.perform(get("/api/v1/logout")
                    .header("Authorization", "Bearer " + "validtoken"))
                .andExpect(status().isNoContent());
        verify(service, times(1)).blacklistToken("validtoken");
    }
}
