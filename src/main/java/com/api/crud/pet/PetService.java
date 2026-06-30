package com.api.crud.pet;

import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
import com.api.crud.pet.dto.FindPetsRequest;
import com.api.crud.pet.dto.PetListResponse;
import com.api.crud.pet.dto.PetRegisterRequest;
import com.api.crud.storage.S3ServiceImpl;
import com.api.crud.tenant.TenantContext;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetService implements IPetService {

    private static final String BUCKET_NAME = "fedepet-imgs";
    private static final String INIT_PATH = "users";

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3ServiceImpl s3Service;

    @Override
    public List<PetListResponse> findAll() {
        List<PetRegistration> list = petRepository.findAll();
        List<PetListResponse> listResponse = new ArrayList<>();
        for (PetRegistration pet : list) {
            listResponse.add(PetListResponse.builder()
                    .breed(pet.getBreed())
                    .name(pet.getName())
                    .species(pet.getSpecies())
                    .dateBirth(pet.getDateBirth())
                    .weight(pet.getWeight())
                    .build());
        }
        return listResponse;
    }

    @Override
    public List<PetListResponse> findById(FindPetsRequest request) {
        UUID clinicId = TenantContext.getCurrentTenant();
        List<PetRegistration> petslist = (clinicId != null)
                ? petRepository.findByUserIdAndUserClinicId(request.getId(), clinicId)
                : petRepository.findByUserId(request.getId());

        List<PetListResponse> petResponse = new ArrayList<>();
        for (PetRegistration petDb : petslist) {
            petResponse.add(PetListResponse.builder()
                    .breed(petDb.getBreed())
                    .dateBirth(petDb.getDateBirth())
                    .name(petDb.getName())
                    .species(petDb.getSpecies())
                    .weight(petDb.getWeight())
                    .photo(petDb.getPhoto())
                    .id(petDb.getId())
                    .build());
        }
        return petResponse;
    }

    @Override
    public void save(PetRegisterRequest pet) {
        Long userId = pet.getOwner();
        UserInfo findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ResponseCode.USER_NOT_FOUND));

        String pathAndNameFile = String.format("%s/%d/%s.png", INIT_PATH, userId, pet.getName());
        if (s3Service.uploadFile(BUCKET_NAME, pathAndNameFile, pet.getPhoto())) {
            String urlImg = String.format("https://fedepet-imgs.s3.us-east-1.amazonaws.com/%s/%d/%s.png", INIT_PATH, userId, pet.getName());
            PetRegistration petDB = PetRegistration.builder()
                    .breed(pet.getBreed())
                    .dateBirth(pet.getDateBirth())
                    .user(findUser)
                    .weight(pet.getWeight())
                    .species(pet.getSpecies())
                    .name(pet.getName())
                    .photo(urlImg)
                    .build();
            petRepository.save(petDB);
        }
    }

    @Override
    public PetRegistration update(Long id, PetRegistration pet) {
        Optional<PetRegistration> existing = petRepository.findById(id);
        if (existing.isPresent()) {
            PetRegistration updated = existing.get();
            updated.setName(pet.getName());
            updated.setSpecies(pet.getSpecies());
            updated.setBreed(pet.getBreed());
            updated.setDateBirth(pet.getDateBirth());
            updated.setWeight(pet.getWeight());
            updated.setUser(pet.getUser());
            return petRepository.save(updated);
        }
        throw new ApiException(ResponseCode.PET_NOT_FOUND);
    }

    @Override
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
