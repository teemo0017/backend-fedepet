package com.api.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime dateTime;
    private String motive;
    private String state;
    private String type;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetRegistration pet;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserInfo client;
}