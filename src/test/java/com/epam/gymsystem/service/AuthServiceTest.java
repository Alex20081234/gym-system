package com.epam.gymsystem.service;

import com.epam.gymsystem.dao.AuthDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private AuthDaoImpl dao;
    @InjectMocks
    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void selectPasswordShouldTryToSelectPasswordFromDatabase() {
        when(dao.selectPassword(anyString())).thenReturn("password");
        String password = service.selectPassword("Test.User");
        assertEquals("password", password);
        verify(dao, times(1)).selectPassword("Test.User");
    }
}
