package com.api.crud.controllers;

import com.api.crud.controllers.dto.pets.FindPetsRequest;
import com.api.crud.controllers.dto.pets.PetListResponse;
import com.api.crud.controllers.dto.pets.PetRegisterRequest;
import com.api.crud.models.PetRegistration;
import com.api.crud.repositories.interfaces.IPetRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*") // Permite acceso desde tu frontend Ionic
public class PetRegistrationController {

    @Autowired
    private IPetRegistrationService petService;

    @GetMapping("/findall")
    public ResponseEntity<List<PetListResponse>> getAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @PostMapping("/findbyuser")
    public ResponseEntity<List<PetListResponse>> getAllByUser(@RequestBody FindPetsRequest req) {
        return ResponseEntity.ok(petService.findById(req));
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PetRegisterRequest pet) {
        petService.save(pet);
        return ResponseEntity.ok("ok");
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
}
