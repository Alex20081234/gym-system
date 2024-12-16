package com.epam.gymsystem.security;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymSystemUserDetailsServiceTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<User> query;
    @InjectMocks
    private GymSystemUserDetailsService userDetailsService;

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
        User mockUser = new Trainee();
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.of(mockUser));
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserNonexistent() {
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonExistentUser"));
    }
}
