package com.api.crud.controllers;

import com.api.crud.controllers.dto.dates.*;
import com.api.crud.repositories.services.DatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dates")
@CrossOrigin(origins = "*") // Permite acceso desde tu frontend Ionic
public class DatesController {

    @Autowired
    private DatesService iDatesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody SaveDateRequest date) {
        iDatesService.save(date);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "Pet saved"
        ));
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody UpdateDateStatus request) {
        iDatesService.updateStateDate(request);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "Pet saved"
        ));
    }

    @PostMapping("/findbyclient")
    public ResponseEntity<List<DateListResponse>> findByClient(@RequestBody FindDatesRequest req) {
        return ResponseEntity.ok(iDatesService.getDatesByUser(req));
    }

    @PostMapping("/findbydoctor")
    public ResponseEntity<AllDatesDoctorResponse> findByDoctor(@RequestBody FindDatesRequest req) {
        return ResponseEntity.ok(iDatesService.getDatesByDoctor(req.getId()));
    }
}
