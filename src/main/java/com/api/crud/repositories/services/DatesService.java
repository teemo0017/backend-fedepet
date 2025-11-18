package com.api.crud.repositories.services;

import com.api.crud.controllers.dto.dates.DateListResponse;
import com.api.crud.controllers.dto.dates.FindDatesRequest;
import com.api.crud.controllers.dto.dates.SaveDateRequest;
import com.api.crud.models.Dates;
import com.api.crud.models.Doctor;
import com.api.crud.models.PetRegistration;
import com.api.crud.models.UserInfo;
import com.api.crud.repositories.interfaces.IDatesService;
import com.api.crud.repositories.repo.IDatesRepository;
import com.api.crud.repositories.repo.IDoctorRepository;
import com.api.crud.repositories.repo.IPetRegistrationRepository;
import com.api.crud.repositories.repo.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatesService implements IDatesService {

    @Autowired
    private IDatesRepository iDatesRepository;

    @Autowired
    private IPetRegistrationRepository iPetRegistrationRepository;

    @Autowired
    private IDoctorRepository iDoctorRepository;

    @Autowired
    private IUserRepository iUserRepository;

    // 🔹 Buscar todas las citas por doctor
    @Transactional
    public List<DateListResponse> getDatesByDoctor(Long doctorId) {
        Doctor findDoctor = iDoctorRepository.findByUserId(doctorId).orElseThrow(() -> new RuntimeException("Doctor no encontrado con ID: " + doctorId));
        List<Dates> datesList = iDatesRepository.findByDoctorId(findDoctor.getId());
        List<DateListResponse> listResponse = new ArrayList<>();
        for (Dates datesdb : datesList) {
            DateListResponse resp = DateListResponse.builder()
                    .id(datesdb.getId())
                    .petImg(datesdb.getPet().getPhoto())
                    .pet(datesdb.getPet().getName())
                    .client(datesdb.getClient().getName())
                    .type(datesdb.getType())
                    .dateTime(datesdb.getDateTime())
                    .motive(datesdb.getMotive())
                    .state(datesdb.getState())
                    .build();
            listResponse.add(resp);
        }
        return listResponse;
    }

    // 🔹 Buscar todas las citas por mascota
    @Transactional
    public List<DateListResponse> getDatesByPet(FindDatesRequest findDatesRequest) {
        List<Dates> datesList = iDatesRepository.findByPetId(findDatesRequest.getId());
        List<DateListResponse> listResponse = new ArrayList<>();
        for (Dates datesdb : datesList) {
            DateListResponse resp = DateListResponse.builder()
                    .doctor(datesdb.getDoctor().getUser().getName())
                    .dateTime(datesdb.getDateTime())
                    .motive(datesdb.getMotive())
                    .state(datesdb.getState())
                    .build();

            listResponse.add(resp);
        }
        return listResponse;
    }

    // 🔹 Buscar todas las citas por Usuario
    @Transactional
    public List<DateListResponse> getDatesByUser(FindDatesRequest findDatesRequest) {
        List<Dates> datesList = iDatesRepository.findByClientId(findDatesRequest.getId());

        List<DateListResponse> listResponse = new ArrayList<>();
        for (Dates datesdb : datesList) {
            DateListResponse resp = DateListResponse.builder()
                    .id(datesdb.getId())
                    .type(datesdb.getType())
                    .doctor(datesdb.getDoctor().getUser().getName())
                    .doctorSpeciality(datesdb.getDoctor().getSpecialty())
                    .doctorImg(datesdb.getDoctor().getPhoto())
                    .petImg(datesdb.getPet().getPhoto())
                    .pet(datesdb.getPet().getName())
                    .dateTime(datesdb.getDateTime())
                    .motive(datesdb.getMotive())
                    .state(datesdb.getState())
                    .build();
            listResponse.add(resp);
        }
        return listResponse;
    }

    public void save(SaveDateRequest request) {
        Doctor findDoctor = iDoctorRepository.findById(request.getDoctor()).orElseThrow(() -> new RuntimeException("Doctor no encontrado con ID: " + request.getDoctor()));
        PetRegistration findPet = iPetRegistrationRepository.findById(request.getPet()).orElseThrow(() -> new RuntimeException("Mascota no encontrado con ID: " + request.getPet()));
        UserInfo findUser =
                iUserRepository.findById(request.getClient()).orElseThrow(() -> new RuntimeException(
                        "Usuario" +
                                " no encontrado con ID: " + request.getPet()));

        Dates dates = Dates.builder()
                .dateTime(request.getDateTime())
                .doctor(findDoctor)
                .type(request.getType())
                .pet(findPet)
                .client(findUser)
                .state("PENDIENTE")
                .motive(request.getMotive())
                .build();
        iDatesRepository.save(dates);
    }

}
