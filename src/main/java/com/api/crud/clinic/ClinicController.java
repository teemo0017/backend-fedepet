package com.api.crud.clinic;

import com.api.crud.clinic.dto.ClinicResponse;
import com.api.crud.clinic.dto.CreateClinicRequest;
import com.api.crud.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/clinic")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ClinicResponse>> create(@RequestBody CreateClinicRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(clinicService.createClinic(request)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'USUARIO')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ClinicResponse>> getMyClinic() {
        return ResponseEntity.ok(ApiResponse.ok(clinicService.getCurrentClinic()));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ClinicResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(clinicService.getAllClinics()));
    }
}
