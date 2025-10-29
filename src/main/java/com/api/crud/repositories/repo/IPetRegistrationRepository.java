package com.api.crud.repositories.repo;

import com.api.crud.models.PetRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPetRegistrationRepository extends JpaRepository<PetRegistration, Long> {
    List<PetRegistration> findByUserId(Long petId);
}
