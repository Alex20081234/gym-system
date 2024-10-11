package com.epam.task.gymsystem.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Passwords {
    private String oldPassword;
    private String newPassword;
}
