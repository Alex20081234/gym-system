package com.epam.gymsystem.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Builder
@Data
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainingtype")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TrainingTypeName", nullable = false)
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers;
}
