package com.epam.task.gymsystem.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Trainee extends User {
    private LocalDateTime dateOfBirth;
    private String address;
    private Long userId;
}
