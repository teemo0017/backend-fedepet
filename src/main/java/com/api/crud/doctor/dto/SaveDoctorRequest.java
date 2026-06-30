package com.api.crud.doctor.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SaveDoctorRequest implements Serializable {
    private String name;
    private String password;
    private String specialty;
    private String description;
    private String email;
    private String phone;
    private String photo;
}
