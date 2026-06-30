package com.api.crud.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleTokenInfo {
    private String email;
    private String name;
    private String sub;

    @JsonProperty("email_verified")
    private String emailVerified;
}
