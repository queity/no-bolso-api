package com.nobolso.api.repository;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.dto.response.PageResponseDTO;
import com.nobolso.api.dto.response.ResumoCategoriaDTO;
import com.nobolso.api.model.Comprovante;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepository {
    Transacao adicionar(TransacaoInputDTO input, Comprovante comprovante, Usuario usuario);
    Optional<Transacao> buscarPorId(Long id, Long usuarioId);
    List<Transacao> pesquisar(TransacaoFilterDTO filters);
    PageResponseDTO<Transacao> pesquisarPaginado(TransacaoFilterDTO filters, int page, int size);
    Optional<Transacao> atualizar(Long id, TransacaoInputDTO input, Comprovante comprovante, Long usuarioId);
    boolean deletar(Long id, Long usuarioId);
    List<Transacao> buscarUltimasTransacoes(int limit, Long usuarioId);
    List<ResumoCategoriaDTO> resumoPorCategoria(TransacaoFilterDTO filters);
}
