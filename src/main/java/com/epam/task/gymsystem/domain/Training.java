package com.epam.task.gymsystem.domain;

import java.time.Duration;
import java.util.Date;

public class Training {

    private Long trainingId;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDate;
    private Duration duration;

    public Training(Long trainingId, Long traineeId, Long trainerId, String trainingName, TrainingType trainingType, Date trainingDate, Duration duration) {
        this.trainingId = trainingId;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }

    public Training() {}

    public Long getTrainingId() {
        return trainingId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Training training = (Training) obj;
        return trainingId.equals(training.getTrainingId()) && traineeId.equals(training.getTraineeId()) && trainerId.equals(training.getTrainerId()) &&
                trainingName.equals(training.getTrainingName()) && trainingType.equals(training.getTrainingType()) && trainingDate.equals(training.getTrainingDate()) && duration.equals(training.getDuration());
    }
}
