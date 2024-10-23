package com.epam.gymsystem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseTrainee extends ShortUser {
    private String dateOfBirth;
    private String address;
    private Boolean isActive;
    private List<ShortTrainer> trainers;
}
