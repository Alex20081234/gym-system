package com.epam.gymsystem.common;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dto.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class MappingUtilsTest {

    @Test
    void fromRequestTraineeToTraineeShouldMapCorrectly() {
        RequestTrainee requestTrainee = new RequestTrainee();
        requestTrainee.setFirstName("John");
        requestTrainee.setLastName("Doe");
        requestTrainee.setDateOfBirth("2000-01-01");
        requestTrainee.setAddress("123 Street");
        Trainee trainee = MappingUtils.fromRequestTraineeToTrainee(requestTrainee);
        assertThat(trainee.getFirstName()).isEqualTo("John");
        assertThat(trainee.getLastName()).isEqualTo("Doe");
        assertThat(trainee.getDateOfBirth()).isEqualTo(LocalDate.parse("2000-01-01"));
        assertThat(trainee.getAddress()).isEqualTo("123 Street");
        assertThat(trainee.getIsActive()).isTrue();
    }

    @Test
    void fromExtendedRequestTraineeToTraineeShouldMapCorrectly() {
        ExtendedRequestTrainee extendedRequestTrainee = new ExtendedRequestTrainee();
        extendedRequestTrainee.setFirstName("Jane");
        extendedRequestTrainee.setLastName("Smith");
        extendedRequestTrainee.setDateOfBirth("1995-05-15");
        extendedRequestTrainee.setAddress("456 Avenue");
        extendedRequestTrainee.setIsActive("true");
        Trainee trainee = MappingUtils.fromExtendedRequestTraineeToTrainee(extendedRequestTrainee);
        assertThat(trainee.getFirstName()).isEqualTo("Jane");
        assertThat(trainee.getLastName()).isEqualTo("Smith");
        assertThat(trainee.getDateOfBirth()).isEqualTo(LocalDate.parse("1995-05-15"));
        assertThat(trainee.getAddress()).isEqualTo("456 Avenue");
        assertThat(trainee.getIsActive()).isTrue();
    }

    @Test
    void fromRequestTrainerToTrainerShouldMapCorrectly() {
        RequestTrainer requestTrainer = new RequestTrainer();
        requestTrainer.setFirstName("Anna");
        requestTrainer.setLastName("Lee");
        ShortTrainingType specialization = ShortTrainingType.builder().build();
        specialization.setId(1);
        specialization.setName("Yoga");
        requestTrainer.setSpecialization(specialization);
        Trainer trainer = MappingUtils.fromRequestTrainerToTrainer(requestTrainer);
        assertThat(trainer.getFirstName()).isEqualTo("Anna");
        assertThat(trainer.getLastName()).isEqualTo("Lee");
        assertThat(trainer.getSpecialization().getId()).isEqualTo(1);
        assertThat(trainer.getSpecialization().getName()).isEqualTo("Yoga");
        assertThat(trainer.getIsActive()).isTrue();
    }

    @Test
    void fromExtendedRequestTrainerToTrainerShouldMapCorrectly() {
        ExtendedRequestTrainer extendedRequestTrainer = new ExtendedRequestTrainer();
        extendedRequestTrainer.setFirstName("Tom");
        extendedRequestTrainer.setLastName("Smith");
        ShortTrainingType specialization = ShortTrainingType.builder().build();
        specialization.setId(2);
        specialization.setName("Weightlifting");
        extendedRequestTrainer.setSpecialization(specialization);
        extendedRequestTrainer.setIsActive("true");
        Trainer trainer = MappingUtils.fromExtendedRequestTrainerToTrainer(extendedRequestTrainer);
        assertThat(trainer.getFirstName()).isEqualTo("Tom");
        assertThat(trainer.getLastName()).isEqualTo("Smith");
        assertThat(trainer.getSpecialization().getId()).isEqualTo(2);
        assertThat(trainer.getSpecialization().getName()).isEqualTo("Weightlifting");
        assertThat(trainer.getIsActive()).isTrue();
    }

    @Test
    void fromTrainerToShortTrainerShouldMapCorrectly() {
        Trainer trainer = Trainer.builder()
                .firstName("Anna")
                .lastName("Lee")
                .username("anna123")
                .specialization(TrainingType.builder().id(1).name("Yoga").build())
                .build();
        ShortTrainer shortTrainer = MappingUtils.fromTrainerToShortTrainer(trainer);
        assertThat(shortTrainer.getUsername()).isEqualTo("anna123");
        assertThat(shortTrainer.getFirstName()).isEqualTo("Anna");
        assertThat(shortTrainer.getLastName()).isEqualTo("Lee");
        assertThat(shortTrainer.getSpecialization().getName()).isEqualTo("Yoga");
    }

    @Test
    void fromTraineeToShortTraineeShouldMapCorrectly() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john123")
                .build();
        ShortTrainee shortTrainee = MappingUtils.fromTraineeToShortTrainee(trainee);
        assertThat(shortTrainee.getUsername()).isEqualTo("john123");
        assertThat(shortTrainee.getFirstName()).isEqualTo("John");
        assertThat(shortTrainee.getLastName()).isEqualTo("Doe");
    }

    @Test
    void fromTraineeToResponseTraineeShouldMapCorrectly() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("123 Street")
                .isActive(true)
                .trainers(new HashSet<>())
                .build();
        Trainer trainer = Trainer.builder().firstName("Anna").specialization(TrainingType.builder().name("Yoga").build()).build();
        trainee.setTrainers(Set.of(trainer));
        ResponseTrainee responseTrainee = MappingUtils.fromTraineeToResponseTrainee(trainee);
        assertThat(responseTrainee.getFirstName()).isEqualTo("John");
        assertThat(responseTrainee.getLastName()).isEqualTo("Doe");
        assertThat(responseTrainee.getTrainers()).hasSize(1);
        assertThat(responseTrainee.getTrainers().get(0).getFirstName()).isEqualTo("Anna");
    }

    @Test
    void fromTrainerToResponseTrainerShouldMapCorrectly() {
        Trainer trainer = Trainer.builder()
                .firstName("Anna")
                .lastName("Lee")
                .specialization(TrainingType.builder().name("Yoga").build())
                .isActive(true)
                .build();
        Trainee trainee = Trainee.builder().firstName("John").build();
        trainer.setTrainees(Set.of(trainee));
        ResponseTrainer responseTrainer = MappingUtils.fromTrainerToResponseTrainer(trainer);
        assertThat(responseTrainer.getFirstName()).isEqualTo("Anna");
        assertThat(responseTrainer.getLastName()).isEqualTo("Lee");
        assertThat(responseTrainer.getSpecialization().getName()).isEqualTo("Yoga");
        assertThat(responseTrainer.getTrainees()).hasSize(1);
        assertThat(responseTrainer.getTrainees().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void fromTraineeToResponseTraineeWithUsernameShouldMapCorrectly() {
        Trainee trainee = Trainee.builder()
                .username("john123")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("123 Street")
                .isActive(true)
                .trainers(new HashSet<>())
                .build();
        Trainer trainer = Trainer.builder().firstName("Anna").specialization(TrainingType.builder().name("Yoga").build()).build();
        trainee.setTrainers(Set.of(trainer));
        ResponseTraineeWithUsername responseTrainee = MappingUtils.fromTraineeToResponseTraineeWithUsername(trainee);
        assertThat(responseTrainee.getUsername()).isEqualTo("john123");
        assertThat(responseTrainee.getFirstName()).isEqualTo("John");
        assertThat(responseTrainee.getLastName()).isEqualTo("Doe");
        assertThat(responseTrainee.getTrainers()).hasSize(1);
        assertThat(responseTrainee.getTrainers().get(0).getFirstName()).isEqualTo("Anna");
    }

    @Test
    void fromTrainerToResponseTrainerWithUsernameShouldMapCorrectly() {
        Trainer trainer = Trainer.builder()
                .username("anna123")
                .firstName("Anna")
                .lastName("Lee")
                .specialization(TrainingType.builder().name("Yoga").build())
                .isActive(true)
                .build();
        Trainee trainee = Trainee.builder().firstName("John").build();
        trainer.setTrainees(Set.of(trainee));
        ResponseTrainerWithUsername responseTrainer = MappingUtils.fromTrainerToResponseTrainerWithUsername(trainer);
        assertThat(responseTrainer.getUsername()).isEqualTo("anna123");
        assertThat(responseTrainer.getFirstName()).isEqualTo("Anna");
        assertThat(responseTrainer.getLastName()).isEqualTo("Lee");
        assertThat(responseTrainer.getSpecialization().getName()).isEqualTo("Yoga");
        assertThat(responseTrainer.getTrainees()).hasSize(1);
        assertThat(responseTrainer.getTrainees().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void fromRequestTrainingToTrainingShouldMapCorrectly() {
        RequestTraining requestTraining = RequestTraining.builder().build();
        requestTraining.setName("Yoga");
        requestTraining.setDate("2023-10-01");
        requestTraining.setTraineeUsername("John");
        requestTraining.setTrainerUsername("Anna");
        requestTraining.setDuration(60);
        Training training = MappingUtils.fromRequestTrainingToTraining(requestTraining);
        assertThat(training.getTrainingName()).isEqualTo("Yoga");
        assertThat(training.getTrainingDate()).isEqualTo(LocalDate.of(2023, 10, 1));
        assertThat(training.getDuration()).isEqualTo(60);
    }

    @Test
    void fromTrainingToResponseTrainingShouldMapCorrectly() {
        Trainee trainee = Trainee.builder().username("john123").firstName("John").build();
        Trainer trainer = Trainer.builder().username("anna123").firstName("Anna").build();
        Training training = Training.builder()
                .trainingDate(LocalDate.of(2023, 10, 1))
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("Yoga")
                .duration(60)
                .trainingType(TrainingType.builder().name("Yoga").build())
                .build();
        ResponseTraining responseTraining = MappingUtils.fromTrainingToResponseTraining(training, "john123");
        assertThat(responseTraining.getName()).isEqualTo("Yoga");
        assertThat(responseTraining.getDate()).isEqualTo("2023-10-01");
        assertThat(responseTraining.getDuration()).isEqualTo(60);
        assertThat(responseTraining.getPartnerName()).isEqualTo("anna123");
        assertThat(responseTraining.getType()).isEqualTo(ShortTrainingType.builder().name("Yoga").build());
        trainee = Trainee.builder().username("anna123").firstName("Anna").build();
        trainer = Trainer.builder().username("john123").firstName("John").build();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        responseTraining = MappingUtils.fromTrainingToResponseTraining(training, "john123");
        assertThat(responseTraining.getName()).isEqualTo("Yoga");
        assertThat(responseTraining.getDate()).isEqualTo("2023-10-01");
        assertThat(responseTraining.getDuration()).isEqualTo(60);
        assertThat(responseTraining.getPartnerName()).isEqualTo("anna123");
        assertThat(responseTraining.getType()).isEqualTo(ShortTrainingType.builder().name("Yoga").build());
    }
}
