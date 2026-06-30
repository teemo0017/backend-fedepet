package com.api.crud.appointment.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllDatesDoctorResponse {
    private List<DateListResponse> datesIncoming;
    private List<DateListResponse> datesDone;
}
