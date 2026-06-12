package com.nobolso.api.repository;

import com.nobolso.api.model.Comprovante;

import java.util.Optional;

public interface ComprovanteRepository {
    Comprovante salvar(byte[] bytes, String nome, String contentType);
    Optional<Comprovante> buscarPorId(Long id);
}
