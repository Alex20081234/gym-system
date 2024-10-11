package com.epam.task.gymsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortTrainingType {
    private String name;
    private Integer id;
}
