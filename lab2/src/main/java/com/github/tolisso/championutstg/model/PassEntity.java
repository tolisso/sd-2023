package com.github.tolisso.championutstg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class PassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long passRecordId;

    private Long passId;

    private String userName;

    private LocalDate dueDate;

}
