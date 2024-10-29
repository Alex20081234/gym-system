package com.epam.gymsystem.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwtService = new JwtService();
    private Authentication authentication;
    private String testToken;
    private int counter = 0;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 600000);
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
        jwtService.blacklistToken(testToken);
        assertTrue(jwtService.isTokenBlackListed(testToken));
    }

    @Test
    void isTokenBlackListedShouldReturnFalseWhenTokenNotBlacklisted() {
        assertFalse(jwtService.isTokenBlackListed(testToken));
    }

    @Test
    void blacklistTokenShouldAddTokenToBlacklist() {
        jwtService.blacklistToken(testToken);
        assertTrue(jwtService.isTokenBlackListed(testToken));
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
    }

    void resetAuthentication() {
        User user = new User("username" + counter,
                "password" + counter, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }
}
