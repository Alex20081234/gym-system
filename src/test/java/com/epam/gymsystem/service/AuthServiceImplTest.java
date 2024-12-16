package com.epam.gymsystem.service;

import com.epam.gymsystem.dao.AuthDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthDao dao;
    @InjectMocks
    private AuthServiceImpl service;

    @Test
    void selectPasswordShouldTryToSelectPasswordFromDatabase() {
        when(dao.selectPassword(anyString())).thenReturn("password");
        String password = service.selectPassword("Test.User");
        assertEquals("password", password);
        verify(dao, times(1)).selectPassword("Test.User");
    }
}
