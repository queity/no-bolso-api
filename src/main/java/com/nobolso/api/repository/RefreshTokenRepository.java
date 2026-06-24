package com.nobolso.api.repository;

import com.nobolso.api.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken salvar(RefreshToken token);
    Optional<RefreshToken> buscarPorToken(String token);
    void revogarPorUsuario(Long usuarioId);
}
