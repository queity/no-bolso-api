package com.nobolso.api.service;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.exception.TransacaoNaoEncontradaException;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.repository.TransacaoRepository;
import com.nobolso.api.util.SaldoCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public Transacao adicionar(TransacaoInputDTO input) {
        return repository.adicionar(input);
    }

    public Transacao buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    public List<Transacao> pesquisar(TransacaoFilterDTO filters) {
        if (filters.dataInicio() != null && filters.dataFim() != null &&
                filters.dataInicio().isAfter(filters.dataFim())) {
            throw new IllegalArgumentException("Data de início não pode ser maior que a data fim");
        }
        return repository.pesquisar(filters);
    }

    public Transacao atualizar(Long id, TransacaoInputDTO input) {
        return repository.atualizar(id, input)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    public void deletar(Long id) {
        if (!repository.deletar(id)) throw new TransacaoNaoEncontradaException(id);
    }

    public BigDecimal buscarSaldo(TransacaoFilterDTO filters) {
        if (filters.dataInicio() != null && filters.dataFim() != null &&
                filters.dataInicio().isAfter(filters.dataFim())) {
            throw new IllegalArgumentException("Data de início não pode ser maior que a data fim");
        }
        return SaldoCalculator.calcular(repository.pesquisar(filters));
    }

    public List<Transacao> buscarUltimasTransacoes(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("O limite deve ser um número positivo");
        return repository.buscarUltimasTransacoes(limit);
    }
}
