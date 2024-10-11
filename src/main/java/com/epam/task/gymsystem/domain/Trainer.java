package com.epam.task.gymsystem.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Trainer")
public class Trainer extends User {
    @ManyToOne
    @JoinColumn(name = "Specialization", referencedColumnName = "Id", nullable = false)
    private TrainingType specialization;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.REMOVE)
    private List<Training> trainings;
    @ManyToMany(mappedBy = "trainers", cascade = CascadeType.REMOVE)
    private Set<Trainee> trainees = new HashSet<>();
}
