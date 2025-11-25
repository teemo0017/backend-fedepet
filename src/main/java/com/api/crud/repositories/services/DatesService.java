package com.api.crud.repositories.services;

import com.api.crud.controllers.dto.dates.*;
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
import java.util.Arrays;
import java.util.List;

@Service
public class DatesService implements IDatesService {
    private static final List<String> STATES_DONE = new ArrayList<>(
            Arrays.asList("CANCELADA", "FINALIZADA")
    );

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
    public AllDatesDoctorResponse getDatesByDoctor(Long doctorId) {
        AllDatesDoctorResponse response = new AllDatesDoctorResponse();
        List<DateListResponse> datesIncoming = new ArrayList<>();
        List<DateListResponse> datesDone = new ArrayList<>();
        Doctor findDoctor = iDoctorRepository.findByUserId(doctorId).orElseThrow(() -> new RuntimeException("Doctor no encontrado con ID: " + doctorId));
        List<Dates> datesList = iDatesRepository.findByDoctorId(findDoctor.getId());

        datesList
                .forEach((date) -> {
                    if (STATES_DONE.contains(date.getState())) {
                        datesDone.add(this.transformDate(date));
                    } else {
                        datesIncoming.add(this.transformDate(date));
                    }
                });
        response.setDatesDone(datesDone);
        response.setDatesIncoming(datesIncoming);

        return response;
    }

    private DateListResponse transformDate(Dates datesdb) {
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

        return resp;
    }

    // 🔹 Buscar todas las citas por doctor
    @Transactional
    public void updateStateDate(UpdateDateStatus request) {
        Dates findDate = iDatesRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + request.getId()));
        findDate.setState(request.getState());
        iDatesRepository.save(findDate);
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
