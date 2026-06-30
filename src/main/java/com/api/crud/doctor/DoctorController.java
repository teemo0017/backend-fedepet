package com.api.crud.doctor;

import com.api.crud.common.ApiResponse;
import com.api.crud.doctor.dto.DoctorResponse;
import com.api.crud.doctor.dto.SaveDoctorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody SaveDoctorRequest request) {
        doctorService.saveDoctor(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @PostMapping("/findall")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(doctorService.getAllDoctors()));
    }
}
