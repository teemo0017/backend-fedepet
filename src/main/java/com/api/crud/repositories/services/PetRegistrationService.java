package com.api.crud.repositories.services;

import com.api.crud.controllers.dto.pets.FindPetsRequest;
import com.api.crud.controllers.dto.pets.PetListResponse;
import com.api.crud.controllers.dto.pets.PetRegisterRequest;
import com.api.crud.models.PetRegistration;
import com.api.crud.models.UserInfo;
import com.api.crud.repositories.interfaces.IPetRegistrationService;
import com.api.crud.repositories.repo.IPetRegistrationRepository;
import com.api.crud.repositories.repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PetRegistrationService implements IPetRegistrationService {

    @Autowired
    private IPetRegistrationRepository petRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<PetListResponse> findAll() {
        List<PetRegistration> list = petRepository.findAll();
        List<PetListResponse> listResponse = new ArrayList<>();
        for (PetRegistration pet : list) {
            PetListResponse petResponse = PetListResponse.builder()
                    .breed(pet.getBreed())
                    .name(pet.getName())
                    .species(pet.getSpecies())
                    .dateBirth(pet.getDateBirth())
                    .weight(pet.getWeight())
                    .build();
            listResponse.add(petResponse);
        }
        return listResponse;
    }

    @Override
    public List<PetListResponse> findById(FindPetsRequest request) {
        List<PetRegistration> petslist = petRepository.findByUserId(request.getId());
        List<PetListResponse> petResponse = new ArrayList<>();

        for (PetRegistration petDb : petslist) {
            PetListResponse pet = PetListResponse.builder()
                    .breed(petDb.getBreed())
                    .dateBirth(petDb.getDateBirth())
                    .name(petDb.getName())
                    .species(petDb.getSpecies())
                    .weight(petDb.getWeight())
                    .build();
            petResponse.add(pet);
        }
        return petResponse;
    }

    @Override
    public void save(PetRegisterRequest pet) {
        UserInfo finduser = userRepository.findById(pet.getOwner()).orElseThrow(() -> new RuntimeException("Responsable no encontrado con ID: " + pet.getOwner()));
        PetRegistration petDB = PetRegistration.builder()
                .breed(pet.getBreed())
                .dateBirth((pet.getDateBirth()))
                .user(finduser)
                .weight(pet.getWeight())
                .species(pet.getSpecies())
                .name(pet.getName())
                .build();
        petRepository.save(petDB);
    }

    @Override
    public PetRegistration update(Long id, PetRegistration pet) {
        Optional<PetRegistration> existingPet = petRepository.findById(id);
        if (existingPet.isPresent()) {
            PetRegistration updated = existingPet.get();
            updated.setName(pet.getName());
            updated.setSpecies(pet.getSpecies());
            updated.setBreed(pet.getBreed());
            updated.setDateBirth(pet.getDateBirth());
            updated.setWeight(pet.getWeight());
            updated.setUser(pet.getUser());
            return petRepository.save(updated);
        } else {
            throw new RuntimeException("Mascota no encontrada con id: " + id);
        }
    }

    @Override
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
