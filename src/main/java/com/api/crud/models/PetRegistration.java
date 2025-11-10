package com.api.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pet_registration")
public class PetRegistration implements Serializable {
    private static final long serialVersionUID = -3138191497993461942L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photo;
    
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500, nullable = false)
    private String species;

    @Column(length = 500, nullable = false)
    private String breed;

    @Column(nullable = false)
    private OffsetDateTime dateBirth;

    @Column()
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Dates> dateId;
}
