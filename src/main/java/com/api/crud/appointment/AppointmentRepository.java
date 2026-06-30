package com.api.crud.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Dates, Long> {
    List<Dates> findByDoctorId(Long doctorId);
    List<Dates> findByDoctorIdAndDoctorUserClinicId(Long doctorId, UUID clinicId);
    Optional<Dates> findById(Long id);
    List<Dates> findByPetId(Long petId);
    List<Dates> findByPetIdAndPetUserClinicId(Long petId, UUID clinicId);
    List<Dates> findByClientId(Long clientId);
    List<Dates> findByClientIdAndClientClinicId(Long clientId, UUID clinicId);
}
