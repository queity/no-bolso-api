package com.nobolso.api.repository.mock;

import com.nobolso.api.model.RefreshToken;
import com.nobolso.api.repository.RefreshTokenRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Profile("dev")
@Repository
public class MockRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<String, RefreshToken> store = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public RefreshToken salvar(RefreshToken token) {
        if (token.getId() == null) {
            token = RefreshToken.builder()
                    .id(nextId.getAndIncrement())
                    .token(token.getToken())
                    .usuario(token.getUsuario())
                    .expiresAt(token.getExpiresAt())
                    .revogado(token.isRevogado())
                    .build();
        }
        store.put(token.getToken(), token);
        return token;
    }

    @Override
    public Optional<RefreshToken> buscarPorToken(String token) {
        return Optional.ofNullable(store.get(token));
    }

    @Override
    public void revogarPorUsuario(Long usuarioId) {
        store.values().stream()
                .filter(t -> t.getUsuario().getId().equals(usuarioId) && !t.isRevogado())
                .forEach(t -> {
                    RefreshToken revogado = RefreshToken.builder()
                            .id(t.getId())
                            .token(t.getToken())
                            .usuario(t.getUsuario())
                            .expiresAt(t.getExpiresAt())
                            .revogado(true)
                            .build();
                    store.put(t.getToken(), revogado);
                });
    }
}
