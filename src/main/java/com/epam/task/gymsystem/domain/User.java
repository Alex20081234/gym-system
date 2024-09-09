package com.epam.task.gymsystem.domain;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public abstract class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    private final Random random = new Random();

    protected User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        password = generatePassword();
        isActive = true;
    }

    protected User() {}

    private String generatePassword() {
        byte[] bytePassword = new byte[10];
        random.nextBytes(bytePassword);
        return new String(bytePassword, StandardCharsets.UTF_8);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
