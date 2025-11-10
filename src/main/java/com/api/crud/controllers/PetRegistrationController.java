package com.api.crud.controllers;

import com.api.crud.config.services.impl.S3Impl;
import com.api.crud.controllers.dto.SaveImgRequest;
import com.api.crud.controllers.dto.pets.FindPetsRequest;
import com.api.crud.controllers.dto.pets.PetListResponse;
import com.api.crud.controllers.dto.pets.PetRegisterRequest;
import com.api.crud.models.PetRegistration;
import com.api.crud.repositories.interfaces.IPetRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*") // Permite acceso desde tu frontend Ionic
public class PetRegistrationController {

    @Autowired
    private IPetRegistrationService petService;

    @Autowired
    private S3Impl s3Service;

    @GetMapping("/findall")
    public ResponseEntity<List<PetListResponse>> getAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @PostMapping("/findbyuser")
    public ResponseEntity<List<PetListResponse>> getAllByUser(@RequestBody FindPetsRequest req) {
        return ResponseEntity.ok(petService.findById(req));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody PetRegisterRequest pet) {
        petService.save(pet);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "Pet saved"
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetRegistration> update(@PathVariable Long id, @RequestBody PetRegistration pet) {
        return ResponseEntity.ok(petService.update(id, pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createBucket")
    public ResponseEntity<String> createBucket(@RequestParam String nameBucket) {
        return ResponseEntity.ok(s3Service.createBucket(nameBucket));
    }

    @PostMapping("/saveimg")
    public ResponseEntity<Boolean> saveImg(@RequestBody SaveImgRequest request) {

        return ResponseEntity.ok(s3Service.uploadFile(request.getBucket(), request.getKey(), request.getBase64()));
    }
}
