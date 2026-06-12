package com.nobolso.api.repository;

import com.nobolso.api.model.Comprovante;
import com.nobolso.api.model.Transacao;

import java.util.Optional;

public interface ComprovanteRepository {
    Comprovante salvar(Transacao transacao, byte[] bytes, String nome, String contentType);
    Optional<Comprovante> buscarPorTransacaoId(Long transacaoId);
}
