package com.epam.task.gymsystem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "TraineeId", nullable = false)
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "TrainerId", nullable = false)
    private Trainer trainer;
    @Column(name = "TrainingName")
    private String trainingName;
    @OneToOne
    @JoinColumn(name = "TrainingTypeId", referencedColumnName = "Id")
    private TrainingType trainingType;
    @Column(name = "TrainingDate")
    private LocalDate trainingDate;
    @Column(name = "TrainingDuration")
    private int duration;
}
