package com.epam.gymsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTraining {
    private String name;
    private String date;
    private ShortTrainingType type;
    private int duration;
    private String partnerName;
}
