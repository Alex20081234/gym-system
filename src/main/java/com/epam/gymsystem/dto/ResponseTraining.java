package com.epam.gymsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTraining {
    private String name;
    private String date;
    private ShortTrainingType type;
    private int duration;
    private String partnerName;
}
