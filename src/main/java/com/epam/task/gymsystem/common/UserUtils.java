package com.epam.task.gymsystem.common;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.User;
import java.util.List;
import java.util.Random;

public class UserUtils {
    private static final Random random = new Random();
    private static int passwordLength = 10;
    private static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private UserUtils() {}

    public static void setUsernameAndPassword(User user, List<String> userNames) {
        user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), userNames));
        user.setPassword(generatePassword());
    }

    private static String generatePassword() {
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
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
