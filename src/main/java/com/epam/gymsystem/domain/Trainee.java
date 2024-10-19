package com.epam.gymsystem.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
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
@Table(name = "Trainee")
public class Trainee extends User {
    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Address")
    private String address;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE)
    private List<Training> trainings;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers = new HashSet<>();

    public void setTrainers(Set<Trainer> trainers) {
        this.trainers.addAll(trainers);
    }

    public void removeTrainers(Set<Trainer> trainers) {
        this.trainers.removeAll(trainers);
    }
}
