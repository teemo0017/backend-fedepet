package com.api.crud.pet.dto;

import lombok.Data;

@Data
public class SaveImgRequest {
    private String bucket;
    private String key;
    private String base64;
}
