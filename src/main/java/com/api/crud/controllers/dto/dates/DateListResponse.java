package com.api.crud.controllers.dto.dates;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateListResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -402687205777262333L;
    private Long id;
    private OffsetDateTime dateTime;
    private String motive;
    private String type;
    private String state;
    private String doctor;
    private String client;
    private String pet;
    private String doctorImg;
    private String petImg;
    private String doctorSpeciality;
}
