package com.epam.gymsystem.security;

import com.epam.gymsystem.dao.BlacklistDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @Mock
    private BlacklistDao dao;
    private JwtService jwtService;
    private Authentication authentication;
    private String testToken;
    private int counter = 0;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(dao, "A2L8YVx0gfXUJpA5p3lBzX9K8klcmXUOvPjH4FbbJCI=", 600000);
        resetAuthentication();
        testToken = jwtService.generateJwtToken(authentication);
        counter++;
    }

    @Test
    void generateJwtTokenShouldReturnValidToken() {
        assertNotNull(testToken);
        assertTrue(jwtService.validateJwtToken(testToken));
    }

    @Test
    void getUserNameFromJwtTokenShouldReturnCorrectUsername() {
        String username = jwtService.getUserNameFromJwtToken(testToken);
        assertEquals("username" + (counter - 1), username);
    }

    @Test
    void validateJwtTokenShouldReturnTrueWhenTokenValid() {
        assertTrue(jwtService.validateJwtToken(testToken));
    }

    @Test
    void validateJwtTokenShouldReturnFalseWhenTokenInvalid() {
        String invalidToken = "invalidToken";
        assertFalse(jwtService.validateJwtToken(invalidToken));
    }

    @Test
    void isTokenBlackListedShouldReturnTrueWhenTokenBlacklisted() {
        when(dao.isBlacklisted(testToken)).thenReturn(true);
        assertTrue(jwtService.isTokenBlackListed(testToken));
        verify(dao, times(1)).isBlacklisted(testToken);
    }

    @Test
    void isTokenBlackListedShouldReturnFalseWhenTokenNotBlacklisted() {
        when(dao.isBlacklisted(testToken)).thenReturn(false);
        assertFalse(jwtService.isTokenBlackListed(testToken));
        verify(dao, times(1)).isBlacklisted(testToken);
    }

    @Test
    void blacklistTokenShouldTryToAddTokenToBlacklist() {
        doNothing().when(dao).blacklist(any());
        when(dao.isBlacklisted(testToken)).thenReturn(true);
        jwtService.blacklistToken(testToken);
        assertTrue(jwtService.isTokenBlackListed(testToken));
        verify(dao, times(1)).blacklist(any());
    }

    @Test
    void parseJwtShouldReturnTokenFromAuthorizationHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + testToken);
        String parsedToken = jwtService.parseJwt(request);
        assertEquals(testToken, parsedToken);
    }

    @Test
    void parseJwtShouldReturnNullWhenInvalidAuthorizationHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "InvalidHeader " + testToken);
        assertNull(jwtService.parseJwt(request));
        request.addHeader("Authorization", "");
        assertNull(jwtService.parseJwt(request));
        MockHttpServletRequest requestWithoutHeader = new MockHttpServletRequest();
        assertNull(jwtService.parseJwt(requestWithoutHeader));
    }

    void resetAuthentication() {
        User user = new User("username" + counter,
                "password" + counter, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }
}
