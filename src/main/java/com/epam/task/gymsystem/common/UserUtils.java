package com.epam.task.gymsystem.common;

import com.epam.task.gymsystem.configuration.GymSystemConfiguration;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;
import java.util.Random;

public class UserUtils {
    private static final Random RANDOM = new AnnotationConfigApplicationContext(GymSystemConfiguration.class).getBean(Random.class);
    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SessionFactory SESSION_FACTORY = new AnnotationConfigApplicationContext(GymSystemConfiguration.class).getBean(SessionFactory.class);

    private UserUtils() {}

    public static void setUsernameAndPassword(User user) {
        user.setUsername(generateUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());
    }

    private static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public static String generateUsername(String firstName, String lastName) {
        List<String> userNames;
        try {
            SESSION_FACTORY.getCurrentSession().beginTransaction();
            userNames = SESSION_FACTORY.getCurrentSession().createQuery("SELECT username FROM User", String.class).list();
            SESSION_FACTORY.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
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

    public static User mergeUsers(User initial, User updates) {
        if (updates == null) {
            return initial;
        }
        if (initial == null) {
            return updates;
        }
        boolean nameWasChanged = false;
        if (updates.getId() != 0) {
            initial.setId(updates.getId());
        }
        if (updates.getFirstName() != null) {
            initial.setFirstName(updates.getFirstName());
            nameWasChanged = true;
        }
        if (updates.getLastName() != null) {
            initial.setLastName(updates.getLastName());
            nameWasChanged = true;
        }
        if (nameWasChanged) {
            initial.setUsername(generateUsername(initial.getFirstName(), initial.getLastName()));
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
        }
        return initial;
    }
}
