package com.api.crud.controllers;

import com.api.crud.controllers.dto.dates.DateListResponse;
import com.api.crud.controllers.dto.dates.FindDatesRequest;
import com.api.crud.controllers.dto.dates.SaveDateRequest;
import com.api.crud.repositories.services.DatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dates")
@CrossOrigin(origins = "*") // Permite acceso desde tu frontend Ionic
public class DatesController {

    @Autowired
    private DatesService iDatesService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody SaveDateRequest date) {
        iDatesService.save(date);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/findbypet")
    public ResponseEntity<List<DateListResponse>> findByPet(@RequestBody FindDatesRequest req) {
        return ResponseEntity.ok(iDatesService.getDatesByPet(req));
    }

    @PostMapping("/findbydoctor")
    public ResponseEntity<List<DateListResponse>> findByDoctor(@RequestBody FindDatesRequest req) {
        return ResponseEntity.ok(iDatesService.getDatesByDoctor(req.getId()));
    }
}
