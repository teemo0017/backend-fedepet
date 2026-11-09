package com.api.crud.clinic;

import com.api.crud.clinic.dto.ClinicResponse;
import com.api.crud.clinic.dto.CreateClinicRequest;
import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
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

    @Transactional
    public ClinicResponse createClinic(CreateClinicRequest request) {
        String slug = request.getClinicSlug().toLowerCase().trim().replaceAll("\\s+", "-");
        if (clinicRepository.findBySlug(slug).isPresent()) {
            throw new ApiException(ResponseCode.SLUG_ALREADY_EXISTS);
        }

        Clinic.ClinicBuilder clinicBuilder = Clinic.builder()
                .slug(slug)
                .name(request.getClinicName())
                .address(request.getClinicAddress())
                .phone(request.getClinicPhone())
                .email(request.getClinicEmail())
                .active(true);
        if (request.getPrimaryColor() != null && !request.getPrimaryColor().isBlank()) {
            clinicBuilder.primaryColor(request.getPrimaryColor());
        }
        Clinic clinic = clinicBuilder.build();
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

        return toResponse(clinic);
    }

    public ClinicResponse getPublicInfo(String slug) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(ResponseCode.SLUG_NOT_FOUND));
        return toResponse(clinic);
    }

    public ClinicResponse getCurrentClinic() {
        UUID clinicId = TenantContext.getCurrentTenant();
        if (clinicId == null) throw new ApiException(ResponseCode.NO_CLINIC_IN_SESSION);
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ApiException(ResponseCode.CLINIC_NOT_FOUND));
        return toResponse(clinic);
    }

    public List<ClinicResponse> getAllClinics() {
        return clinicRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ClinicResponse toResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .slug(clinic.getSlug())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .logo(clinic.getLogo())
                .primaryColor(clinic.getPrimaryColor())
                .active(clinic.isActive())
                .build();
    }
}
