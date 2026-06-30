package com.api.crud.appointment;

import com.api.crud.appointment.dto.*;
import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
import com.api.crud.doctor.Doctor;
import com.api.crud.doctor.DoctorRepository;
import com.api.crud.pet.PetRegistration;
import com.api.crud.pet.PetRepository;
import com.api.crud.tenant.TenantContext;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private static final List<String> STATES_DONE = new ArrayList<>(Arrays.asList("CANCELADA", "FINALIZADA"));

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AllDatesDoctorResponse getDatesByDoctor(Long doctorId) {
        UUID clinicId = TenantContext.getCurrentTenant();
        Doctor findDoctor = (clinicId != null)
                ? doctorRepository.findByUserIdAndUserClinicId(doctorId, clinicId)
                        .orElseThrow(() -> new ApiException(ResponseCode.DOCTOR_NOT_FOUND))
                : doctorRepository.findByUserId(doctorId)
                        .orElseThrow(() -> new ApiException(ResponseCode.DOCTOR_NOT_FOUND));

        List<Dates> datesList = (clinicId != null)
                ? appointmentRepository.findByDoctorIdAndDoctorUserClinicId(findDoctor.getId(), clinicId)
                : appointmentRepository.findByDoctorId(findDoctor.getId());

        List<DateListResponse> incoming = new ArrayList<>();
        List<DateListResponse> done = new ArrayList<>();
        datesList.forEach(date -> {
            if (STATES_DONE.contains(date.getState())) done.add(transformDate(date));
            else incoming.add(transformDate(date));
        });

        AllDatesDoctorResponse response = new AllDatesDoctorResponse();
        response.setDatesIncoming(incoming);
        response.setDatesDone(done);
        return response;
    }

    private DateListResponse transformDate(Dates d) {
        return DateListResponse.builder()
                .id(d.getId())
                .petImg(d.getPet().getPhoto())
                .pet(d.getPet().getName())
                .client(d.getClient().getName())
                .type(d.getType())
                .dateTime(d.getDateTime())
                .motive(d.getMotive())
                .state(d.getState())
                .build();
    }

    @Transactional
    public void updateStateDate(UpdateDateStatus request) {
        Dates findDate = appointmentRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException(ResponseCode.DATE_NOT_FOUND));
        findDate.setState(request.getState());
        appointmentRepository.save(findDate);
    }

    @Transactional
    public List<DateListResponse> getDatesByPet(FindDatesRequest request) {
        UUID clinicId = TenantContext.getCurrentTenant();
        List<Dates> datesList = (clinicId != null)
                ? appointmentRepository.findByPetIdAndPetUserClinicId(request.getId(), clinicId)
                : appointmentRepository.findByPetId(request.getId());

        List<DateListResponse> list = new ArrayList<>();
        for (Dates d : datesList) {
            list.add(DateListResponse.builder()
                    .doctor(d.getDoctor().getUser().getName())
                    .dateTime(d.getDateTime())
                    .motive(d.getMotive())
                    .state(d.getState())
                    .build());
        }
        return list;
    }

    @Transactional
    public List<DateListResponse> getDatesByUser(FindDatesRequest request) {
        UUID clinicId = TenantContext.getCurrentTenant();
        List<Dates> datesList = (clinicId != null)
                ? appointmentRepository.findByClientIdAndClientClinicId(request.getId(), clinicId)
                : appointmentRepository.findByClientId(request.getId());

        List<DateListResponse> list = new ArrayList<>();
        for (Dates d : datesList) {
            list.add(DateListResponse.builder()
                    .id(d.getId())
                    .type(d.getType())
                    .doctor(d.getDoctor().getUser().getName())
                    .doctorSpeciality(d.getDoctor().getSpecialty())
                    .doctorImg(d.getDoctor().getPhoto())
                    .petImg(d.getPet().getPhoto())
                    .pet(d.getPet().getName())
                    .dateTime(d.getDateTime())
                    .motive(d.getMotive())
                    .state(d.getState())
                    .build());
        }
        return list;
    }

    public void save(SaveDateRequest request) {
        Doctor findDoctor = doctorRepository.findById(request.getDoctor())
                .orElseThrow(() -> new ApiException(ResponseCode.DOCTOR_NOT_FOUND));
        PetRegistration findPet = petRepository.findById(request.getPet())
                .orElseThrow(() -> new ApiException(ResponseCode.PET_NOT_FOUND));
        UserInfo findUser = userRepository.findById(request.getClient())
                .orElseThrow(() -> new ApiException(ResponseCode.USER_NOT_FOUND));

        Dates dates = Dates.builder()
                .dateTime(request.getDateTime())
                .doctor(findDoctor)
                .type(request.getType())
                .pet(findPet)
                .client(findUser)
                .state("PENDIENTE")
                .motive(request.getMotive())
                .build();
        appointmentRepository.save(dates);
    }
}
