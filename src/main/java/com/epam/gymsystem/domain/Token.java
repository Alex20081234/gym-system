package com.epam.gymsystem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Builder
@Entity
@Table(name = "blacklist")
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    private String id;

    @Column(name = "ExpireDate")
    private LocalDate expireDate;
}
