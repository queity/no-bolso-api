package com.nobolso.api.service;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.exception.TransacaoNaoEncontradaException;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.repository.ComprovanteRepository;
import com.nobolso.api.repository.TransacaoRepository;
import com.nobolso.api.util.SaldoCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private final ComprovanteRepository comprovanteRepository;

    public TransacaoService(TransacaoRepository repository, ComprovanteRepository comprovanteRepository) {
        this.repository = repository;
        this.comprovanteRepository = comprovanteRepository;
    }

    @Transactional
    public Transacao adicionar(TransacaoInputDTO input) {
        log.info("Adicionando transação: valor={}, direcao={}", input.valor(), input.direcao());
        return repository.adicionar(input);
    }

    @Transactional
    public Transacao adicionarComprovante(Long id, MultipartFile arquivo) {
        log.info("Adicionando comprovante para transação id={}", id);
        Transacao transacao = buscarPorId(id);
        try {
            comprovanteRepository.salvar(transacao, arquivo.getBytes(),
                    arquivo.getOriginalFilename(), arquivo.getContentType());
            return transacao;
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Transacao buscarPorId(Long id) {
        log.debug("Buscando transação por id={}", id);
        return repository.buscarPorId(id)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    @Transactional(readOnly = true)
    public List<Transacao> pesquisar(TransacaoFilterDTO filters) {
        validarPeriodo(filters.dataInicio(), filters.dataFim());
        return repository.pesquisar(filters);
    }

    @Transactional
    public Transacao atualizar(Long id, TransacaoInputDTO input) {
        log.info("Atualizando transação id={}", id);
        return repository.atualizar(id, input)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando transação id={}", id);
        if (!repository.deletar(id)) throw new TransacaoNaoEncontradaException(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal buscarSaldo(TransacaoFilterDTO filters) {
        validarPeriodo(filters.dataInicio(), filters.dataFim());
        return SaldoCalculator.calcular(repository.pesquisar(filters));
    }

    @Transactional(readOnly = true)
    public List<Transacao> buscarUltimasTransacoes(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("O limite deve ser um número positivo");
        return repository.buscarUltimasTransacoes(limit);
    }

    private void validarPeriodo(java.time.LocalDateTime dataInicio, java.time.LocalDateTime dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser maior que a data fim");
        }
    }
}
