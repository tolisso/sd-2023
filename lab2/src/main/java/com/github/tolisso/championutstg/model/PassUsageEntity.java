package com.github.tolisso.championutstg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PassUsageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long passUsageId;

    private String userName;

    private LocalDateTime timestamp;

    // true  - enter
    // false - exit
    private boolean isEnter;
}
