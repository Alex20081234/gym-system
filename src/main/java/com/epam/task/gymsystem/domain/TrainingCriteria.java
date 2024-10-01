package com.epam.task.gymsystem.domain;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder
@Data
public class TrainingCriteria {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String partnerUsername;
    private TrainingType trainingType;
}
