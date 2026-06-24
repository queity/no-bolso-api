package com.nobolso.api.repository;

import com.nobolso.api.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);
}
