package com.nobolso.api.repository;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.model.Comprovante;
import com.nobolso.api.model.Transacao;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepository {
    Transacao adicionar(TransacaoInputDTO input, Comprovante comprovante);
    Optional<Transacao> buscarPorId(Long id);
    List<Transacao> pesquisar(TransacaoFilterDTO filters);
    Optional<Transacao> atualizar(Long id, TransacaoInputDTO input, Comprovante comprovante);
    boolean deletar(Long id);
    List<Transacao> buscarUltimasTransacoes(int limit);
}
