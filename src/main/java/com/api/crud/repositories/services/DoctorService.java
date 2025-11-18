package com.api.crud.repositories.services;

import com.api.crud.config.services.interfaces.IS3Service;
import com.api.crud.controllers.dto.doctor.DoctorResponse;
import com.api.crud.controllers.dto.doctor.SaveDoctorRequest;
import com.api.crud.models.Doctor;
import com.api.crud.models.Role;
import com.api.crud.models.UserInfo;
import com.api.crud.repositories.repo.IDoctorRepository;
import com.api.crud.repositories.repo.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final String BUCKET_NAME = "fedepet-imgs";
    private final String INIT_PATH = "doctors";
    @Autowired
    private IDoctorRepository doctorRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IS3Service s3Service;

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponse> doctorResponses = new ArrayList<>();
        for (Doctor doc : doctors) {
            DoctorResponse doctorResponse = DoctorResponse.builder()
                    .email(doc.getUser().getEmail())
                    .available(doc.isAvailable())
                    .name(doc.getUser().getName())
                    .description(doc.getDescription())
                    .specialty(doc.getSpecialty())
                    .phone(doc.getUser().getPhone())
                    .photo(doc.getPhoto())
                    .id(doc.getId())
                    .build();
            doctorResponses.add(doctorResponse);
        }
        return doctorResponses;
    }

    @Transactional
    public void saveDoctor(SaveDoctorRequest request) {
        String nameRandom = UUID.randomUUID().toString();
        String pathImg = String.format("%s/%s.png", INIT_PATH, nameRandom);
        if (s3Service.uploadFile(BUCKET_NAME, pathImg, request.getPhoto())) {
            UserInfo userInfo = UserInfo.builder()
                    .name(request.getName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(Role.DOCTOR)
                    .phone(request.getPhone())
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

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
