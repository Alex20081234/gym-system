package com.epam.gymsystem.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Builder
@Entity
@Table(name = "UserTries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTries {
    @Id
    private String id;

    @Column(name = "Attempts")
    private Integer attempts;

    @Column(name = "BlockTime")
    private Long blockTime;

    @Column(name = "ExpireDate")
    private LocalDate expireDate;
}
