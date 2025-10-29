package com.api.crud.controllers.dto.doctor;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SaveDoctorRequest implements Serializable {
    private String name;
    private String specialty;
    private String email;
    private String phone;
}
