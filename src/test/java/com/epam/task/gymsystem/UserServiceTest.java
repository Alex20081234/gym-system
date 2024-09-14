package com.epam.task.gymsystem;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.User;
import com.epam.task.gymsystem.service.UserService;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void testGenerateUsername() {
        Map<Long, User> userMap = new HashMap<>();
        User user1 = Trainer.builder().firstName("Alice").lastName("Smith").build();
        User user2 = Trainee.builder().firstName("Alice").lastName("Smith").build();
        userMap.put(1L, user1);
        userMap.put(2L, user2);
        String username1 = UserService.generateUsername("Alice", "Smith", userMap);
        user1.setUsername(username1);
        assertEquals("Alice.Smith", username1);
        String username2 = UserService.generateUsername("Alice", "Smith", userMap);
        assertEquals("Alice.Smith1", username2);
    }

    @Test
    void testSetUsernameAndPassword() {
        Map<Long, User> userMap = new HashMap<>();
        User user = Trainer.builder().firstName("Alice").lastName("Smith").build();
        userMap.put(1L, user);
        UserService.setUsernameAndPassword(user, userMap);
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
    }
}