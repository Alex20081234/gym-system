package com.epam.task.gymsystem.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"trainings", "trainees"})
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Trainer")
public class Trainer extends User {
    @ManyToOne
    @JoinColumn(name = "Specialization", referencedColumnName = "Id")
    private TrainingType specialization;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Training> trainings;
    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<Trainee> trainees = new HashSet<>();
}
