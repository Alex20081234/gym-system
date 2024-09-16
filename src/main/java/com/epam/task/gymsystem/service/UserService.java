package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.configuration.GymSystemConfiguration;
import com.epam.task.gymsystem.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class UserService {
    private static final Random RANDOM = new AnnotationConfigApplicationContext(GymSystemConfiguration.class).getBean(Random.class);
    private static final int PASSWORD_LENGTH = 10;

    private UserService() {}

    public static void setUsernameAndPassword(User user, Map<Long, ? extends User> map) {
        user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), map));
        user.setPassword(generatePassword());
    }

    private static String generatePassword() {
        byte[] bytePassword = new byte[PASSWORD_LENGTH];
        RANDOM.nextBytes(bytePassword);
        return new String(bytePassword, StandardCharsets.UTF_8);
    }

    public static String generateUsername(String firstName, String lastName, Map<Long, ? extends User> map) {
        StringBuilder username = new StringBuilder(firstName + "." + lastName);
        int counter = 0;
        while (exists(username.toString(), map)) {
            counter++;
            if (counter == 1) {
                username.append(counter);
                continue;
            }
            username = new StringBuilder(username.substring(0, username.length() - String.valueOf(counter - 1).length()));
            username.append(counter);
        }
        return username.toString();
    }

    private static boolean exists(String username, Map<Long, ? extends User> map) {
        for (User user : map.values()) {
            if (user.getUsername() != null && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
