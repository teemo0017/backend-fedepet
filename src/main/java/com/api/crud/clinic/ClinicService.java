package com.api.crud.clinic;

import com.api.crud.clinic.dto.ClinicResponse;
import com.api.crud.clinic.dto.CreateClinicRequest;
import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
import com.api.crud.jwt.JwtService;
import com.api.crud.tenant.TenantContext;
import com.api.crud.user.Role;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public ClinicResponse createClinic(CreateClinicRequest request) {
        Clinic clinic = Clinic.builder()
                .name(request.getClinicName())
                .address(request.getClinicAddress())
                .phone(request.getClinicPhone())
                .email(request.getClinicEmail())
                .active(true)
                .build();
        clinic = clinicRepository.save(clinic);

        UserInfo admin = UserInfo.builder()
                .name(request.getAdminName())
                .email(request.getAdminEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .phone(request.getAdminPhone())
                .role(Role.ADMIN)
                .clinic(clinic)
                .build();
        admin = userRepository.save(admin);

        return toResponse(clinic, jwtService.getToken(admin));
    }

    public ClinicResponse getCurrentClinic() {
        UUID clinicId = TenantContext.getCurrentTenant();
        if (clinicId == null) throw new ApiException(ResponseCode.NO_CLINIC_IN_SESSION);
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ApiException(ResponseCode.CLINIC_NOT_FOUND));
        return toResponse(clinic, null);
    }

    public List<ClinicResponse> getAllClinics() {
        return clinicRepository.findAll().stream()
                .map(c -> toResponse(c, null))
                .collect(Collectors.toList());
    }

    private ClinicResponse toResponse(Clinic clinic, String adminToken) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .logo(clinic.getLogo())
                .active(clinic.isActive())
                .adminToken(adminToken)
                .build();
    }
}
