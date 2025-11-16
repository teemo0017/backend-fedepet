package com.api.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String specialty;
    private String email;
    private String phone;
    private String photo;
    private String description;
    private boolean available = true;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Dates> dates;
}