package com.api.crud.repositories.repo;

import com.api.crud.models.Dates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDatesRepository extends JpaRepository<Dates, Long> {

    // 🔹 Buscar todas las citas de un doctor por su ID
    List<Dates> findByDoctorId(Long doctorId);

    Optional<Dates> findById(Long id);

    // 🔹 Buscar todas las citas de una mascota por su ID
    List<Dates> findByPetId(Long petId);

    List<Dates> findByClientId(Long clientId);
}
