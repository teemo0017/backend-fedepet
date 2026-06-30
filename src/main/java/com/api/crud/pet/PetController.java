package com.api.crud.pet;

import com.api.crud.common.ApiResponse;
import com.api.crud.pet.dto.FindPetsRequest;
import com.api.crud.pet.dto.PetListResponse;
import com.api.crud.pet.dto.PetRegisterRequest;
import com.api.crud.pet.dto.SaveImgRequest;
import com.api.crud.storage.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private IPetService petService;

    @Autowired
    private S3ServiceImpl s3Service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findall")
    public ResponseEntity<ApiResponse<List<PetListResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(petService.findAll()));
    }

    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    @PostMapping("/findbyuser")
    public ResponseEntity<ApiResponse<List<PetListResponse>>> getAllByUser(@RequestBody FindPetsRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(petService.findById(req)));
    }

    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody PetRegisterRequest pet) {
        petService.save(pet);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PetRegistration>> update(@PathVariable Long id, @RequestBody PetRegistration pet) {
        return ResponseEntity.ok(ApiResponse.ok(petService.update(id, pet)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    @PostMapping("/saveimg")
    public ResponseEntity<ApiResponse<Boolean>> saveImg(@RequestBody SaveImgRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(s3Service.uploadFile(request.getBucket(), request.getKey(), request.getBase64())));
    }
}
