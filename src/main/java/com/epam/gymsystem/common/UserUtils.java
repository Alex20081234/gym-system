package com.epam.gymsystem.common;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.User;
import com.epam.gymsystem.dto.Credentials;
import java.util.List;
import java.util.Random;

public final class UserUtils {
    private static final Random RANDOM = new Random();
    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private UserUtils() {}

    public static Credentials setUsernameAndPassword(User user, List<String> userNames) {
        String username = generateUsername(user.getFirstName(), user.getLastName(), userNames);
        String password = generatePassword();
        user.setUsername(username);
        user.setPassword(password);
        return Credentials.builder()
                .username(username)
                .password(password)
                .build();
    }

    private static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public static String generateUsername(String firstName, String lastName, List<String> userNames) {
        StringBuilder username = new StringBuilder(firstName + "." + lastName);
        int counter = 0;
        while (exists(username.toString(), userNames)) {
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

    private static boolean exists(String username, List<String> userNames) {
        for (String currentUsername : userNames) {
            if (currentUsername != null && currentUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static User mergeUsers(User initial, User updates, List<String> userNames) {
        if (updates == null) {
            return initial;
        }
        if (initial == null) {
            return updates;
        }
        boolean nameWasChanged = false;
        if (updates.getFirstName() != null) {
            initial.setFirstName(updates.getFirstName());
            nameWasChanged = true;
        }
        if (updates.getLastName() != null) {
            initial.setLastName(updates.getLastName());
            nameWasChanged = true;
        }
        if (nameWasChanged) {
            initial.setUsername(generateUsername(initial.getFirstName(), initial.getLastName(), userNames));
        }
        if (updates.getPassword() != null) {
            initial.setPassword(updates.getPassword());
        }
        if (updates.getIsActive() != null) {
            initial.setIsActive(updates.getIsActive());
        }
        if (initial instanceof Trainer && updates instanceof Trainer) {
            if (((Trainer) updates).getTrainings() != null) {
                ((Trainer) initial).setTrainings(((Trainer) updates).getTrainings());
            }
            if (((Trainer) updates).getSpecialization() != null) {
                ((Trainer) initial).setSpecialization(((Trainer) updates).getSpecialization());
            }
            if (((Trainer) updates).getTrainees() != null) {
                ((Trainer) initial).setTrainees(((Trainer) updates).getTrainees());
            }
        } else if (initial instanceof Trainee && updates instanceof Trainee) {
            if (((Trainee) updates).getAddress() != null) {
                ((Trainee) initial).setAddress(((Trainee) updates).getAddress());
            }
            if (((Trainee) updates).getDateOfBirth() != null) {
                ((Trainee) initial).setDateOfBirth(((Trainee) updates).getDateOfBirth());
            }
            if (((Trainee) updates).getTrainings() != null) {
                ((Trainee) initial).setTrainings(((Trainee) updates).getTrainings());
            }
            if (((Trainee) updates).getTrainers() != null) {
                ((Trainee) initial).setTrainers(((Trainee) updates).getTrainers());
            }
        }
        return initial;
    }
}
