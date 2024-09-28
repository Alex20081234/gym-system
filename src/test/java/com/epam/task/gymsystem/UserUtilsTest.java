package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserUtilsTest {

    @Test
    void testSetUsernameAndPassword() {
        User user = Trainer.builder().firstName("John").lastName("Doe").build();
        UserUtils.setUsernameAndPassword(user);
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
        assertEquals(10, user.getPassword().length());
        assertEquals("John.Doe", user.getUsername());
    }

    @Test
    void testGenerateUsername() {
        String username = UserUtils.generateUsername("Jane", "Doe");
        assertNotNull(username);
        assertEquals("Jane.Doe", username);
    }

    @Test
    void testMergeUsers() {
        Trainee initial = Trainee.builder()
                .firstName("Alexa")
                .lastName("Smith")
                .password("password")
                .isActive(true)
                .build();
        Trainee updates = Trainee.builder()
                .firstName("Jane")
                .password("newpassword")
                .isActive(false)
                .build();
        User mergedUser = UserUtils.mergeUsers(initial, updates);
        Trainee expected = Trainee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .password("newpassword")
                .isActive(false)
                .build();
        assertEquals(expected, mergedUser);
    }

    @Test
    void testMergeUsersWithNullUpdates() {
        Trainee initial = new Trainee();
        initial.setFirstName("John");
        initial.setLastName("Doe");
        User mergedUser = UserUtils.mergeUsers(initial, null);
        assertEquals(initial, mergedUser);
    }

    @Test
    void testMergeUsersWithNullInitial() {
        Trainee updates = new Trainee();
        updates.setFirstName("Jane");
        updates.setLastName("Smith");
        User mergedUser = UserUtils.mergeUsers(null, updates);
        assertEquals(updates, mergedUser);
    }
}