package com.api.crud.repositories.repo;

import com.api.crud.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String username);

    Optional<UserInfo> findById(Long id);
}
