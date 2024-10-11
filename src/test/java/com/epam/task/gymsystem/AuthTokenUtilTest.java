package com.epam.task.gymsystem;

import com.epam.task.gymsystem.security.AuthTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.AccessDeniedException;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthTokenUtilTest {
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static String token;

    @BeforeEach
    void setUp() {
        token = Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes());
    }

    @Test
    void generateTokenShouldReturnValidToken() {
        String generatedToken = AuthTokenUtil.generateToken(USERNAME, PASSWORD);
        assertEquals(token, generatedToken);
    }

    @Test
    void isValidTokenShouldReturnTrueWhenTokenIsValid() {
        boolean isValid = AuthTokenUtil.isValidToken(token, USERNAME, PASSWORD);
        assertTrue(isValid);
    }

    @Test
    void isValidTokenShouldReturnFalseWhenTokenIsInvalid() {
        boolean isValid = AuthTokenUtil.isValidToken("invalidToken", USERNAME, PASSWORD);
        assertFalse(isValid);
    }

    @Test
    void validateTokenShouldValidateToken() {
        assertDoesNotThrow(() -> AuthTokenUtil.validateToken(USERNAME, PASSWORD, token));
    }

    @Test
    void validateTokenShouldThrowAccessDeniedExceptionWhenTokenIsInvalid() {
        Exception exception = assertThrows(AccessDeniedException.class, () -> AuthTokenUtil.validateToken(USERNAME, PASSWORD, "invalidToken"));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void validateTokenShouldThrowAccessDeniedExceptionWhenTokenIsNull() {
        Exception e = assertThrows(AccessDeniedException.class, () -> AuthTokenUtil.validateToken(USERNAME, PASSWORD, null), "Should throw for null token.");
        assertEquals("Unauthorized access", e.getMessage());
    }

    @Test
    void verifyCookieShouldVerifyCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("authCookie", token);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        assertDoesNotThrow(() -> AuthTokenUtil.verifyCookie(request, USERNAME, PASSWORD));
    }

    @Test
    void verifyCookieShouldThrowAccessDeniedExceptionWhenCookiesAreNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);
        Exception e = assertThrows(AccessDeniedException.class, () -> AuthTokenUtil.verifyCookie(request, USERNAME, PASSWORD), "Should throw if no cookies present.");
        assertEquals("Unauthorized access", e.getMessage());
    }
}

