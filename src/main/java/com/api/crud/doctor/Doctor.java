package com.api.crud.doctor;

import com.api.crud.appointment.Dates;
import com.api.crud.user.UserInfo;
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

    private String specialty;
    private String photo;
    private String description;

    @Builder.Default
    private boolean available = true;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Dates> dates;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;
}
