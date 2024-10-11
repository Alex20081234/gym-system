package com.epam.task.gymsystem.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ResponseTraining {
    private String name;
    private LocalDate date;
    private ShortTrainingType type;
    private int duration;
    private String partnerName;
}
