package com.epam.task.gymsystem;

import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.domain.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserUtilsTest {
    @Test
    void setUsernameAndPasswordShouldGenerateAndSetUsernameAndPassword() {
        User user = Trainer.builder().firstName("John").lastName("Doe").build();
        UserUtils.setUsernameAndPassword(user, Collections.emptyList());
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
        assertEquals(10, user.getPassword().length());
        assertEquals("John.Doe", user.getUsername());
    }

    @Test
    void generateUsernameShouldGenerateUsername() {
        String username = UserUtils.generateUsername("Jane", "Doe", Collections.emptyList());
        assertNotNull(username);
        assertEquals("Jane.Doe", username);
    }

    @Test
    void generateUsernameShouldGenerateUsernameWhenItAlreadyExists() {
        String username = UserUtils.generateUsername("Jane", "Doe", List.of("Jane.Doe"));
        assertNotNull(username);
        assertEquals("Jane.Doe1", username);
        username = UserUtils.generateUsername("Jane", "Doe", List.of("Jane.Doe", "Jane.Doe1"));
        assertNotNull(username);
        assertEquals("Jane.Doe2", username);
    }

    @Test
    void mergeTraineesShouldReturnMerged() {
        Trainee initial = Trainee.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Test address")
                .trainings(new ArrayList<>())
                .trainers(new HashSet<>())
                .build();
        Trainee updates = Trainee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .password("newpassword")
                .isActive(false)
                .address("New address")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .trainings(List.of(Training.builder().id(1).build()))
                .trainers(Set.of(Trainer.builder().username("Test.Trainer").build()))
                .build();
        User mergedUser = UserUtils.mergeUsers(initial, updates, Collections.emptyList());
        Trainee expected = Trainee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .password("newpassword")
                .isActive(false)
                .address("New address")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .trainings(List.of(Training.builder().id(1).build()))
                .trainers(Set.of(Trainer.builder().username("Test.Trainer").build()))
                .build();
        areSameTrainees(expected, (Trainee) mergedUser);
    }

    @Test
    void mergeTrainersShouldReturnMerged() {
        Trainer initial = Trainer.builder()
                .username("Test.Trainee")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .specialization(new TrainingType())
                .trainings(new ArrayList<>())
                .trainees(new HashSet<>())
                .build();
        Trainer updates = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .password("newpassword")
                .isActive(false)
                .specialization(TrainingType.builder().id(1).build())
                .trainings(List.of(Training.builder().id(1).build()))
                .trainees(Set.of(Trainee.builder().username("Test.Trainee").build()))
                .build();
        User mergedUser = UserUtils.mergeUsers(initial, updates, Collections.emptyList());
        Trainer expected = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .password("newpassword")
                .isActive(false)
                .specialization(TrainingType.builder().id(1).build())
                .trainings(List.of(Training.builder().id(1).build()))
                .trainees(Set.of(Trainee.builder().username("Test.Trainee").build()))
                .build();
        areSameTrainers(expected, (Trainer) mergedUser);
    }

    @Test
    void mergeUsersShouldReturnInitialWhenNullUpdates() {
        Trainee initial = new Trainee();
        initial.setFirstName("John");
        initial.setLastName("Doe");
        User mergedUser = UserUtils.mergeUsers(initial, null, Collections.emptyList());
        assertEquals(initial, mergedUser);
    }

    @Test
    void mergeUsersShouldReturnUpdatesWhenNullInitial() {
        Trainee updates = new Trainee();
        updates.setFirstName("Jane");
        updates.setLastName("Smith");
        User mergedUser = UserUtils.mergeUsers(null, updates, Collections.emptyList());
        assertEquals(updates, mergedUser);
    }

    void areSameTrainees(Trainee expected, Trainee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getIsActive(), actual.getIsActive());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getTrainings(), actual.getTrainings());
        assertEquals(expected.getTrainers(), actual.getTrainers());
    }

    void areSameTrainers(Trainer expected, Trainer actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getIsActive(), actual.getIsActive());
        assertEquals(expected.getTrainings(), actual.getTrainings());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        assertEquals(expected.getTrainees(), actual.getTrainees());
    }
}