package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.configuration.GymSystemConfiguration;
import com.epam.gymsystem.domain.Trainee;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GymSystemConfiguration.class})
@WebAppConfiguration
@Transactional
class AuthDaoTest {
    @Autowired
    private AuthDaoImpl authDao;
    @Autowired
    private TraineeDaoImpl traineeDao;
    @Autowired
    private EntityManager entityManager;
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
