package com.api.crud.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetRegistration, Long> {
    List<PetRegistration> findByUserId(Long userId);
    List<PetRegistration> findByUserIdAndUserClinicId(Long userId, UUID clinicId);
    Optional<PetRegistration> findByIdAndUserClinicId(Long petId, UUID clinicId);
}
