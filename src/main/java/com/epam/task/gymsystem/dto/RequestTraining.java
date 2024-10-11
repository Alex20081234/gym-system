package com.epam.task.gymsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestTraining {
    private String traineeUsername;
    private String trainerUsername;
    private String name;
    private String date;
    private int duration;
}
