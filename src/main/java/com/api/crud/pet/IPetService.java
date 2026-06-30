package com.api.crud.pet;

import com.api.crud.pet.dto.FindPetsRequest;
import com.api.crud.pet.dto.PetListResponse;
import com.api.crud.pet.dto.PetRegisterRequest;

import java.util.List;

public interface IPetService {
    List<PetListResponse> findAll();
    List<PetListResponse> findById(FindPetsRequest request);
    void save(PetRegisterRequest pet);
    PetRegistration update(Long id, PetRegistration pet);
    void delete(Long id);
}
