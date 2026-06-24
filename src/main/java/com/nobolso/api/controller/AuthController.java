package com.nobolso.api.controller;

import com.nobolso.api.dto.request.LoginRequestDTO;
import com.nobolso.api.dto.request.RefreshTokenRequestDTO;
import com.nobolso.api.dto.request.RegisterRequestDTO;
import com.nobolso.api.dto.response.AuthResponseDTO;
import com.nobolso.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Registro, login e renovação de tokens")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.registrar(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar access token com refresh token")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        return ResponseEntity.ok(authService.refresh(dto.refreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Encerrar sessão e revogar refresh token")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        authService.logout(dto.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
