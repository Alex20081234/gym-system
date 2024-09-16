package com.epam.task.gymsystem.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Trainer extends User {
    private Long userId;
    private TrainingType specialization;
}
