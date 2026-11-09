package com.api.crud.auth;

import com.api.crud.auth.dto.AuthResponse;
import com.api.crud.auth.dto.LoginRequest;
import com.api.crud.auth.dto.RegisterRequest;
import com.api.crud.clinic.Clinic;
import com.api.crud.clinic.ClinicRepository;
import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
import com.api.crud.jwt.JwtService;
import com.api.crud.user.Role;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse loginSuperAdmin(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserInfo user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new ApiException(ResponseCode.USER_NOT_FOUND));

        if (user.getRole() != Role.SUPER_ADMIN) {
            throw new ApiException(ResponseCode.UNAUTHORIZED);
        }

        return AuthResponse.builder().token(jwtService.getToken(user)).build();
    }

    public AuthResponse login(String slug, LoginRequest request) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(ResponseCode.SLUG_NOT_FOUND));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserInfo user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new ApiException(ResponseCode.USER_NOT_FOUND));

        if (user.getClinic() == null || !user.getClinic().getId().equals(clinic.getId())) {
            throw new ApiException(ResponseCode.CLINIC_MISMATCH);
        }

        return AuthResponse.builder().token(jwtService.getToken(user)).build();
    }

    public AuthResponse register(String slug, RegisterRequest request) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(ResponseCode.SLUG_NOT_FOUND));

        UserInfo userInfo = UserInfo.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail().toLowerCase())
                .role(Role.CLIENTE)
                .phone(request.getPhone())
                .clinic(clinic)
                .build();
        userRepository.save(userInfo);

        return AuthResponse.builder().token(jwtService.getToken(userInfo)).build();
    }
}
