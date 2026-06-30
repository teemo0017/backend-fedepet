package com.api.crud.appointment;

import com.api.crud.appointment.dto.*;
import com.api.crud.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dates")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody SaveDateRequest date) {
        appointmentService.save(date);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Void>> update(@RequestBody UpdateDateStatus request) {
        appointmentService.updateStateDate(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    @PostMapping("/findbyclient")
    public ResponseEntity<ApiResponse<List<DateListResponse>>> findByClient(@RequestBody FindDatesRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getDatesByUser(req)));
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    @PostMapping("/findbydoctor")
    public ResponseEntity<ApiResponse<AllDatesDoctorResponse>> findByDoctor(@RequestBody FindDatesRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getDatesByDoctor(req.getId())));
    }
}
