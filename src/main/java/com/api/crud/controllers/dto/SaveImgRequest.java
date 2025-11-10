package com.api.crud.controllers.dto;

import lombok.Data;

@Data
public class SaveImgRequest {
    String bucket;
    String key;
    String base64;
}
