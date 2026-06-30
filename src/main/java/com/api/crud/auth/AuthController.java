package com.api.crud.auth;

import com.api.crud.auth.dto.AuthResponse;
import com.api.crud.auth.dto.GoogleAuthRequest;
import com.api.crud.auth.dto.LoginRequest;
import com.api.crud.auth.dto.RegisterRequest;
import com.api.crud.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(request)));
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.register(request)));
    }

    @PostMapping("google")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(@RequestBody GoogleAuthRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(googleAuthService.loginWithGoogle(request)));
    }
}
