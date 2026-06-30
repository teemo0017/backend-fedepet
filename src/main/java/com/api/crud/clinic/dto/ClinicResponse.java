package com.api.crud.clinic.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClinicResponse {
    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String logo;
    private boolean active;
    private String adminToken;
}
