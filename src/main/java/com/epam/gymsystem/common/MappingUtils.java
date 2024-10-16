package com.epam.gymsystem.common;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dto.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MappingUtils {

    private MappingUtils() {}

    public static Trainee fromRequestTraineeToTrainee(RequestTrainee requestTrainee) {
        return Trainee.builder()
                .firstName(requestTrainee.getFirstName())
                .lastName(requestTrainee.getLastName())
                .dateOfBirth(LocalDate.parse(requestTrainee.getDateOfBirth()))
                .address(requestTrainee.getAddress())
                .isActive(true)
                .build();
    }

    public static Trainee fromExtendedRequestTraineeToTrainee(ExtendedRequestTrainee extendedRequestTrainee) {
        return Trainee.builder()
                .firstName(extendedRequestTrainee.getFirstName())
                .lastName(extendedRequestTrainee.getLastName())
                .dateOfBirth(LocalDate.parse(extendedRequestTrainee.getDateOfBirth()))
                .address(extendedRequestTrainee.getAddress())
                .isActive(Boolean.parseBoolean(extendedRequestTrainee.getIsActive()))
                .build();
    }

    public static Trainer fromRequestTrainerToTrainer(RequestTrainer requestTrainer) {
        return Trainer.builder()
                .firstName(requestTrainer.getFirstName())
                .lastName(requestTrainer.getLastName())
                .specialization(fromShortTrainingTypeToTrainingType(requestTrainer.getSpecialization()))
                .isActive(true)
                .build();
    }

    public static Trainer fromExtendedRequestTrainerToTrainer(ExtendedRequestTrainer extendedRequestTrainer) {
        return Trainer.builder()
                .firstName(extendedRequestTrainer.getFirstName())
                .lastName(extendedRequestTrainer.getLastName())
                .specialization(fromShortTrainingTypeToTrainingType(extendedRequestTrainer.getSpecialization()))
                .isActive(Boolean.parseBoolean(extendedRequestTrainer.getIsActive()))
                .build();
    }

    public static ShortTrainer fromTrainerToShortTrainer(Trainer trainer) {
        return ShortTrainer.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .specialization(fromTrainingTypeToShortTrainingType(trainer.getSpecialization()))
                .build();
    }

    public static ShortTrainee fromTraineeToShortTrainee(Trainee trainee) {
        return ShortTrainee.builder()
                .username(trainee.getUsername())
                .firstName(trainee.getFirstName())
                .lastName(trainee.getLastName())
                .build();
    }

    public static ResponseTrainee fromTraineeToResponseTrainee(Trainee trainee) {
        List<ShortTrainer> trainers = new ArrayList<>();
        if (trainee.getTrainers() != null) {
            trainee.getTrainers().forEach(trainer -> trainers.add(fromTrainerToShortTrainer(trainer)));
        }
        return ResponseTrainee.builder()
                .firstName(trainee.getFirstName())
                .lastName(trainee.getLastName())
                .dateOfBirth(trainee.getDateOfBirth().toString())
                .address(trainee.getAddress())
                .isActive(trainee.getIsActive())
                .trainers(trainers)
                .build();
    }

    public static ResponseTrainer fromTrainerToResponseTrainer(Trainer trainer) {
        List<ShortTrainee> trainees = new ArrayList<>();
        if (trainer.getTrainees() != null) {
            trainer.getTrainees().forEach(trainee -> trainees.add(fromTraineeToShortTrainee(trainee)));
        }
        return ResponseTrainer.builder()
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .specialization(fromTrainingTypeToShortTrainingType(trainer.getSpecialization()))
                .isActive(trainer.getIsActive())
                .trainees(trainees)
                .build();
    }

    public static ResponseTraineeWithUsername fromTraineeToResponseTraineeWithUsername(Trainee trainee) {
        List<ShortTrainer> trainers = new ArrayList<>();
        if (trainee.getTrainers() != null) {
            trainee.getTrainers().forEach(trainer -> trainers.add(fromTrainerToShortTrainer(trainer)));
        }
        return ResponseTraineeWithUsername.builder()
                .username(trainee.getUsername())
                .firstName(trainee.getFirstName())
                .lastName(trainee.getLastName())
                .dateOfBirth(trainee.getDateOfBirth().toString())
                .address(trainee.getAddress())
                .isActive(trainee.getIsActive())
                .trainers(trainers)
                .build();
    }

    public static ResponseTrainerWithUsername fromTrainerToResponseTrainerWithUsername(Trainer trainer) {
        List<ShortTrainee> trainees = new ArrayList<>();
        if (trainer.getTrainees() != null) {
            trainer.getTrainees().forEach(trainee -> trainees.add(fromTraineeToShortTrainee(trainee)));
        }
        return ResponseTrainerWithUsername.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .specialization(fromTrainingTypeToShortTrainingType(trainer.getSpecialization()))
                .isActive(trainer.getIsActive())
                .trainees(trainees)
                .build();
    }

    public static Training fromRequestTrainingToTraining(RequestTraining requestTraining) {
        return Training.builder()
                .trainingName(requestTraining.getName())
                .trainingDate(LocalDate.parse(requestTraining.getDate()))
                .duration(requestTraining.getDuration())
                .build();
    }

    public static ResponseTraining fromTrainingToResponseTraining(Training training, String ownUsername) {
        return ResponseTraining.builder()
                .name(training.getTrainingName())
                .date(training.getTrainingDate().toString())
                .type(fromTrainingTypeToShortTrainingType(training.getTrainingType()))
                .duration(training.getDuration())
                .partnerName(training.getTrainer().getUsername().equals(ownUsername) ? training.getTrainee().getUsername() : training.getTrainer().getUsername())
                .build();
    }

    public static ShortTrainingType fromTrainingTypeToShortTrainingType(TrainingType trainingType) {
        return ShortTrainingType.builder()
                .id(trainingType.getId())
                .name(trainingType.getName())
                .build();
    }

    public static TrainingType fromShortTrainingTypeToTrainingType(ShortTrainingType requestTrainingType) {
        return TrainingType.builder()
                .name(requestTrainingType.getName())
                .id(requestTrainingType.getId())
                .build();
    }
}
