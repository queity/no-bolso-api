package com.nobolso.api.service;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.exception.TransacaoNaoEncontradaException;
import com.nobolso.api.model.Comprovante;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.ICodigoEnum;
import com.nobolso.api.model.enums.TipoTransacao;
import com.nobolso.api.repository.ComprovanteRepository;
import com.nobolso.api.repository.TransacaoRepository;
import com.nobolso.api.util.SaldoCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TransacaoService {

    private static final int ANOS_MAX_RETROATIVO = 10;

    private final TransacaoRepository repository;
    private final ComprovanteRepository comprovanteRepository;

    public TransacaoService(TransacaoRepository repository, ComprovanteRepository comprovanteRepository) {
        this.repository = repository;
        this.comprovanteRepository = comprovanteRepository;
    }

    @Transactional
    public Transacao adicionar(TransacaoInputDTO input) {
        log.info("Adicionando transação: valor={}, direcao={}", input.valor(), input.direcao());
        validarInput(input);
        Comprovante comprovante = resolverComprovante(input.comprovanteId());
        return repository.adicionar(input, comprovante);
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
        validarInput(input);
        Comprovante comprovante = resolverComprovante(input.comprovanteId());
        return repository.atualizar(id, input, comprovante)
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

    private void validarInput(TransacaoInputDTO input) {
        ICodigoEnum.fromCodigo(TipoTransacao.class, input.tipo());
        ICodigoEnum.fromCodigo(DirecaoTransacao.class, input.direcao());
        if (input.categoria() != null) {
            ICodigoEnum.fromCodigo(CategoriaTransacao.class, input.categoria());
        }
        validarDataTransacao(input.dataTransacao());
    }

    private void validarDataTransacao(LocalDateTime data) {
        LocalDateTime limiteRetroativo = LocalDateTime.now().minusYears(ANOS_MAX_RETROATIVO);
        if (data.isBefore(limiteRetroativo)) {
            throw new IllegalArgumentException(
                    "A data da transação não pode ser anterior a " + ANOS_MAX_RETROATIVO + " anos atrás");
        }
    }

    private Comprovante resolverComprovante(Long comprovanteId) {
        if (comprovanteId == null) return null;
        return comprovanteRepository.buscarPorId(comprovanteId)
                .orElseThrow(() -> new IllegalArgumentException("Comprovante não encontrado: " + comprovanteId));
    }

    private void validarPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser maior que a data fim");
        }
    }
}
