package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthDaoTest {
    @Autowired
    private AuthDaoImpl authDao;
    @Autowired
    private TraineeDaoImpl traineeDao;
    private Trainee initial;

    @BeforeEach
    void setUp() {
        initial = Trainee.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .build();
    }

    void cleanUp(String... usernames) {
        for (String username : usernames) {
            traineeDao.delete(username);
        }
    }

    @Test
    void selectPasswordShouldSelectPasswordFromDatabase() {
        traineeDao.create(initial);
        String password = authDao.selectPassword("Test.Trainee");
        cleanUp("Test.Trainee");
        assertEquals("password", password);
    }

    @Test
    void selectPasswordShouldThrowUserNotFoundExceptionWhenNonExistentUser() {
        RuntimeException e = assertThrows(UserNotFoundException.class, () -> authDao.selectPassword("Non.Existent"));
        assertEquals("User with username Non.Existent was not found", e.getMessage());
    }
}
