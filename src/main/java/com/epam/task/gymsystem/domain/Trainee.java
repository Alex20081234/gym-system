package com.epam.task.gymsystem.domain;

import java.util.Date;

public class Trainee extends User{

    private Date dateOfBirth;
    private String address;
    private Long userId;
    public Trainee(String firstName, String lastName, Date dateOfBirth, String address, Long userId) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
    }

    public Trainee() {}

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
