package com.nobolso.api.dto.response;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String nome,
        String email
) {}
