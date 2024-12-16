package com.epam.gymsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitWorkloadChangesRequestBody {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private Boolean trainerIsActive;
    private LocalDate trainingDate;
    private int trainingDurationMinutes;
    private ActionType changeType;
}
