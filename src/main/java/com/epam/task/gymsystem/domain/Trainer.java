package com.epam.task.gymsystem.domain;

public class Trainer extends User{

    private Long userId;
    private TrainingType specialization;
    public Trainer(String firstName, String lastName, Long userId, TrainingType specialization) {
        super(firstName, lastName);
        this.userId = userId;
        this.specialization = specialization;
    }

    public Trainer() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }
}
