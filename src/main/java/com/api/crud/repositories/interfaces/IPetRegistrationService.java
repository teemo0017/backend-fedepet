package com.api.crud.repositories.interfaces;

import com.api.crud.controllers.dto.pets.FindPetsRequest;
import com.api.crud.controllers.dto.pets.PetListResponse;
import com.api.crud.controllers.dto.pets.PetRegisterRequest;
import com.api.crud.models.PetRegistration;

import java.util.List;

public interface IPetRegistrationService {

    List<PetListResponse> findAll();

    List<PetListResponse> findById(FindPetsRequest request);

    void save(PetRegisterRequest pet);

    PetRegistration update(Long id, PetRegistration pet);

    void delete(Long id);
}
