package com.epam.task.gymsystem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseTrainer extends BaseTrainer {
    private Boolean isActive;
    private List<ShortTrainee> trainees;
}
