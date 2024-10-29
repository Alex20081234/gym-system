package com.epam.gymsystem.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class AuthEntryPointJwtTest {
    private final AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Test
    void commenceShouldReturnUnauthorizedWhenGetsException() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        AuthenticationException authException = Mockito.mock(AuthenticationException.class);
        authEntryPointJwt.commence(request, response, authException);
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
