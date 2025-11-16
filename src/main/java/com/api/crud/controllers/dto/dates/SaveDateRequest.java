package com.api.crud.controllers.dto.dates;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@Data
public class SaveDateRequest implements Serializable {
    private OffsetDateTime dateTime;
    private String motive;
    private String type;
    private Long pet;
    private Long doctor;
    private Long client;
}
