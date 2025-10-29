package com.api.crud.controllers;

import com.api.crud.controllers.dto.doctor.DoctorResponse;
import com.api.crud.controllers.dto.doctor.SaveDoctorRequest;
import com.api.crud.repositories.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*") // Permite acceso desde tu frontend Ionic
public class DoctorsController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody SaveDoctorRequest doctorRequest) {
        doctorService.saveDoctor(doctorRequest);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/findall")
    public ResponseEntity<List<DoctorResponse>> findAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }


}
