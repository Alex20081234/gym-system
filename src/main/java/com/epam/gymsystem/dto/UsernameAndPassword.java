package com.epam.gymsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsernameAndPassword {
    private String username;
    private String password;
}
