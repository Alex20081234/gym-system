package com.epam.gymsystem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseTrainerWithUsername extends ResponseTrainer {
    private String username;
}
