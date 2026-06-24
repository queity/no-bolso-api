package com.nobolso.api.repository.mock;

import com.nobolso.api.model.Usuario;
import com.nobolso.api.model.enums.Role;
import com.nobolso.api.repository.UsuarioRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Profile("dev")
@Repository
public class MockUsuarioRepository implements UsuarioRepository {

    private final Map<Long, Usuario> store = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public MockUsuarioRepository(PasswordEncoder encoder) {
        Usuario dev = Usuario.builder()
                .id(1L)
                .nome("Dev User")
                .email("dev@nobolso.com")
                .senha(encoder.encode("123456"))
                .role(Role.USER)
                .build();
        store.put(dev.getId(), dev);
        nextId.set(2);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario = Usuario.builder()
                    .id(nextId.getAndIncrement())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .senha(usuario.getSenha())
                    .role(usuario.getRole())
                    .build();
        }
        store.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existePorEmail(String email) {
        return store.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
