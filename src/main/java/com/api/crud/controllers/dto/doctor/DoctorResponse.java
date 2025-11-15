package com.api.crud.controllers.dto.doctor;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DoctorResponse implements Serializable {
    private String name;
    private String specialty;
    private String email;
    private String phone;
    private boolean available;
    private String photo;
    private long id;
}
