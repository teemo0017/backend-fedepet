package com.api.crud.repositories.services;

import com.api.crud.controllers.dto.doctor.DoctorResponse;
import com.api.crud.controllers.dto.doctor.SaveDoctorRequest;
import com.api.crud.models.Doctor;
import com.api.crud.repositories.repo.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private IDoctorRepository doctorRepository;

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponse> doctorResponses = new ArrayList<>();
        for (Doctor doc : doctors) {
            DoctorResponse doctorResponse = DoctorResponse.builder()
                    .email(doc.getEmail())
                    .available(doc.isAvailable())
                    .name(doc.getName())
                    .specialty(doc.getSpecialty())
                    .phone(doc.getPhone())
                    .build();
            doctorResponses.add(doctorResponse);
        }
        return doctorResponses;
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor saveDoctor(SaveDoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .phone(request.getPhone())
                .specialty(request.getSpecialty())
                .email(request.getEmail())
                .name(request.getName())
                .build();
        return doctorRepository.save(doctor);
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
