package com.api.crud.config;

import com.api.crud.user.Role;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${super.admin.email}")
    private String superAdminEmail;

    @Value("${super.admin.password}")
    private String superAdminPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(superAdminEmail).isPresent()) return;

        UserInfo superAdmin = UserInfo.builder()
                .name("Super Admin")
                .email(superAdminEmail.toLowerCase())
                .password(passwordEncoder.encode(superAdminPassword))
                .role(Role.SUPER_ADMIN)
                .build();
        userRepository.save(superAdmin);
    }
}
