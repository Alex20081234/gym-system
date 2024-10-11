package com.epam.task.gymsystem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseTrainee extends ShortUser {
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private List<ShortTrainer> trainers;
}
