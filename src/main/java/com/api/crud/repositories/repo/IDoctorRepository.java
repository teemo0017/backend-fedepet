package com.api.crud.repositories.repo;

import com.api.crud.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long id);
}
