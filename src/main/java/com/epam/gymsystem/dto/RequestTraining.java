package com.epam.gymsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestTraining {
    private String traineeUsername;
    private String trainerUsername;
    private String name;
    private String date;
    private int duration;
}
