package com.epam.task.gymsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerRequiredPair {
    private String username;
    private Boolean required;
}
