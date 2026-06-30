package com.api.crud.doctor;

import com.api.crud.clinic.Clinic;
import com.api.crud.clinic.ClinicRepository;
import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
import com.api.crud.doctor.dto.DoctorResponse;
import com.api.crud.doctor.dto.SaveDoctorRequest;
import com.api.crud.storage.IS3Service;
import com.api.crud.tenant.TenantContext;
import com.api.crud.user.Role;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private static final String BUCKET_NAME = "fedepet-imgs";
    private static final String INIT_PATH = "doctors";

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IS3Service s3Service;

    public List<DoctorResponse> getAllDoctors() {
        UUID clinicId = TenantContext.getCurrentTenant();
        List<Doctor> doctors = (clinicId != null)
                ? doctorRepository.findByUserClinicId(clinicId)
                : doctorRepository.findAll();

        List<DoctorResponse> responses = new ArrayList<>();
        for (Doctor doc : doctors) {
            responses.add(DoctorResponse.builder()
                    .email(doc.getUser().getEmail())
                    .available(doc.isAvailable())
                    .name(doc.getUser().getName())
                    .description(doc.getDescription())
                    .specialty(doc.getSpecialty())
                    .phone(doc.getUser().getPhone())
                    .photo(doc.getPhoto())
                    .id(doc.getId())
                    .build());
        }
        return responses;
    }

    @Transactional
    public void saveDoctor(SaveDoctorRequest request) {
        UUID clinicId = TenantContext.getCurrentTenant();
        Clinic clinic = null;
        if (clinicId != null) {
            clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new ApiException(ResponseCode.CLINIC_NOT_FOUND));
        }

        String nameRandom = UUID.randomUUID().toString();
        String pathImg = String.format("%s/%s.png", INIT_PATH, nameRandom);
        if (!s3Service.uploadFile(BUCKET_NAME, pathImg, request.getPhoto())) {
            throw new ApiException(ResponseCode.IMAGE_UPLOAD_ERROR);
        }

        UserInfo userInfo = UserInfo.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.DOCTOR)
                .phone(request.getPhone())
                .clinic(clinic)
                .build();
        userRepository.save(userInfo);

        String urlImg = String.format("https://fedepet-imgs.s3.us-east-1.amazonaws.com/%s/%s.png", INIT_PATH, nameRandom);
        Doctor doctor = Doctor.builder()
                .available(true)
                .description(request.getDescription())
                .specialty(request.getSpecialty())
                .photo(urlImg)
                .user(userInfo)
                .build();
        doctorRepository.save(doctor);
    }
}
