package com.api.crud.controllers.dto.dates;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@Data
public class DateListResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -402687205777262333L;
    private OffsetDateTime dateTime;
    private String motive;
    private String state;
    private String Doctor;
    private String pet;
}
