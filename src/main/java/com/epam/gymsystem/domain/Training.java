package com.epam.gymsystem.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "TraineeId", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "TrainerId", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Trainer trainer;

    @Column(name = "TrainingName", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "TrainingTypeId", nullable = false)
    private TrainingType trainingType;

    @Column(name = "TrainingDate", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "TrainingDuration", nullable = false)
    private int duration;
}
