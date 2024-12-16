package com.epam.gymsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private GymSystemUserDetailsService detailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalShouldAuthenticateWhenValidToken() throws ServletException, IOException {
        String jwt = "valid.jwt.token";
        String username = "testUser";
        when(jwtService.parseJwt(request)).thenReturn(jwt);
        when(jwtService.isTokenBlackListed(jwt)).thenReturn(false);
        when(jwtService.validateJwtToken(jwt)).thenReturn(true);
        when(jwtService.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(detailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);
        authTokenFilter.doFilterInternal(request, response, filterChain);
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        assert authentication.getPrincipal() == userDetails;
        verify(jwtService, times(1)).parseJwt(request);
        verify(jwtService, times(1)).validateJwtToken(jwt);
        verify(detailsService, times(1)).loadUserByUsername(username);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalShouldNotAuthenticateWhenInvalidToken() throws ServletException, IOException {
        String jwt = "invalid.jwt.token";
        when(jwtService.parseJwt(request)).thenReturn(jwt);
        when(jwtService.isTokenBlackListed(jwt)).thenReturn(false);
        when(jwtService.validateJwtToken(jwt)).thenReturn(false);
        authTokenFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService, times(1)).parseJwt(request);
        verify(jwtService, times(1)).validateJwtToken(jwt);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalShouldNotAuthenticateWhenTokenBlacklisted() throws ServletException, IOException {
        String jwt = "blacklisted.jwt.token";
        when(jwtService.parseJwt(request)).thenReturn(jwt);
        when(jwtService.isTokenBlackListed(jwt)).thenReturn(true);
        authTokenFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService, times(1)).parseJwt(request);
        verify(jwtService, times(1)).isTokenBlackListed(jwt);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalShouldNotAuthenticateWhenNoToken() throws ServletException, IOException {
        when(jwtService.parseJwt(request)).thenReturn(null);
        authTokenFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService, times(1)).parseJwt(request);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
