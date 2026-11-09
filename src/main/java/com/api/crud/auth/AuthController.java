package com.api.crud.auth;

import com.api.crud.auth.dto.AuthResponse;
import com.api.crud.auth.dto.GoogleAuthRequest;
import com.api.crud.auth.dto.LoginRequest;
import com.api.crud.auth.dto.RegisterRequest;
import com.api.crud.clinic.ClinicService;
import com.api.crud.clinic.dto.ClinicResponse;
import com.api.crud.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/{slug}")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;
    private final ClinicService clinicService;

    @PostMapping("auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @PathVariable String slug,
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(slug, request)));
    }

    @PostMapping("auth/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @PathVariable String slug,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.register(slug, request)));
    }

    @PostMapping("auth/google")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(
            @PathVariable String slug,
            @RequestBody GoogleAuthRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(googleAuthService.loginWithGoogle(slug, request)));
    }

    @GetMapping("public/info")
    public ResponseEntity<ApiResponse<ClinicResponse>> getPublicInfo(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(clinicService.getPublicInfo(slug)));
    }
}
