package com.api.crud.controllers.dto.dates;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@Data
public class DateListResponse implements Serializable {
    private OffsetDateTime dateTime;
    private String motive;
    private String state;
    private String Doctor;
    private String pet;
}
