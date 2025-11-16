package com.api.crud.repositories.services;

import com.api.crud.config.services.interfaces.IS3Service;
import com.api.crud.controllers.dto.doctor.DoctorResponse;
import com.api.crud.controllers.dto.doctor.SaveDoctorRequest;
import com.api.crud.models.Doctor;
import com.api.crud.repositories.repo.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IS3Service s3Service;

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponse> doctorResponses = new ArrayList<>();
        for (Doctor doc : doctors) {
            DoctorResponse doctorResponse = DoctorResponse.builder()
                    .email(doc.getEmail())
                    .available(doc.isAvailable())
                    .name(doc.getName())
                    .description(doc.getDescription())
                    .specialty(doc.getSpecialty())
                    .phone(doc.getPhone())
                    .photo(doc.getPhoto())
                    .id(doc.getId())
                    .build();
            doctorResponses.add(doctorResponse);
        }
        return doctorResponses;
    }

    public void saveDoctor(SaveDoctorRequest request) {
        String nameRandom = UUID.randomUUID().toString();
        String pathImg = String.format("%s/%s.png", INIT_PATH, nameRandom);

        if (s3Service.uploadFile(BUCKET_NAME, pathImg, request.getPhoto())) {
            String urlImg = String.format("https://fedepet-imgs.s3.us-east-1.amazonaws.com/%s/%s.png", INIT_PATH, nameRandom);
            Doctor doctor = Doctor.builder()
                    .phone(request.getPhone())
                    .available(true)
                    .description(request.getDescription())
                    .specialty(request.getSpecialty())
                    .email(request.getEmail())
                    .name(request.getName())
                    .photo(urlImg)
                    .build();
            doctorRepository.save(doctor);
        }


    }

    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        return doctorRepository.findById(id)
                .map(doctor -> {
                    doctor.setName(doctorDetails.getName());
                    doctor.setSpecialty(doctorDetails.getSpecialty());
                    doctor.setEmail(doctorDetails.getEmail());
                    doctor.setPhone(doctorDetails.getPhone());
                    doctor.setAvailable(doctorDetails.isAvailable());
                    return doctorRepository.save(doctor);
                })
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado con id: " + id));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
