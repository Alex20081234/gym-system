package com.epam.gymsystem.steps;

import io.cucumber.java.After;
import org.springframework.security.core.context.SecurityContextHolder;

public class Hooks {

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
