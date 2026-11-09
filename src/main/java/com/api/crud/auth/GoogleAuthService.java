package com.api.crud.auth;

import com.api.crud.auth.dto.AuthResponse;
import com.api.crud.auth.dto.GoogleAuthRequest;
import com.api.crud.auth.dto.GoogleTokenInfo;
import com.api.crud.clinic.Clinic;
import com.api.crud.clinic.ClinicRepository;
import com.api.crud.common.ApiException;
import com.api.crud.common.ResponseCode;
import com.api.crud.jwt.JwtService;
import com.api.crud.user.Role;
import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    private static final String GOOGLE_TOKENINFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public AuthResponse loginWithGoogle(String slug, GoogleAuthRequest request) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(ResponseCode.SLUG_NOT_FOUND));

        GoogleTokenInfo tokenInfo = verifyGoogleToken(request.getIdToken());

        UserInfo user = userRepository.findByEmail(tokenInfo.getEmail())
                .map(existing -> {
                    if (existing.getClinic() == null || !existing.getClinic().getId().equals(clinic.getId())) {
                        throw new ApiException(ResponseCode.CLINIC_MISMATCH);
                    }
                    return existing;
                })
                .orElseGet(() -> createGoogleUser(tokenInfo, clinic));

        return AuthResponse.builder().token(jwtService.getToken(user)).build();
    }

    private GoogleTokenInfo verifyGoogleToken(String idToken) {
        try {
            GoogleTokenInfo tokenInfo = restTemplate.getForObject(GOOGLE_TOKENINFO_URL + idToken, GoogleTokenInfo.class);
            if (tokenInfo == null || tokenInfo.getEmail() == null) {
                throw new ApiException(ResponseCode.GOOGLE_TOKEN_INVALID);
            }
            return tokenInfo;
        } catch (HttpClientErrorException e) {
            throw new ApiException(ResponseCode.GOOGLE_TOKEN_EXPIRED);
        }
    }

    private UserInfo createGoogleUser(GoogleTokenInfo tokenInfo, Clinic clinic) {
        UserInfo newUser = UserInfo.builder()
                .name(tokenInfo.getName())
                .email(tokenInfo.getEmail().toLowerCase())
                .password(java.util.UUID.randomUUID().toString())
                .role(Role.CLIENTE)
                .clinic(clinic)
                .build();
        return userRepository.save(newUser);
    }
}
