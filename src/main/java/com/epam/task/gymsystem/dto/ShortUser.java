package com.epam.task.gymsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ShortUser {
    private String firstName;
    private String lastName;
}
