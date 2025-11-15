package com.api.crud.repositories.repo;

import com.api.crud.models.Dates;
import com.api.crud.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDatesRepository extends JpaRepository<Dates, Long> {

    // 🔹 Buscar todas las citas de un doctor por su ID
    List<Dates> findByDoctorId(Long doctorId);

    // 🔹 Buscar todas las citas de una mascota por su ID
    List<Dates> findByPetId(Long petId);

    List<Dates> findByClient(Long clientId);
}
