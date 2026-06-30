package com.api.crud.auth.dto;

import lombok.Data;

@Data
public class GoogleAuthRequest {
    private String idToken;
    private String clinicId;
}
