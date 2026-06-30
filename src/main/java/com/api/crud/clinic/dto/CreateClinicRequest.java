package com.api.crud.clinic.dto;

import lombok.Data;

@Data
public class CreateClinicRequest {
    private String clinicName;
    private String clinicAddress;
    private String clinicPhone;
    private String clinicEmail;
    private String adminName;
    private String adminEmail;
    private String adminPassword;
    private String adminPhone;
}
