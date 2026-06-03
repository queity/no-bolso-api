package com.nobolso.api.repository.mock;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import com.nobolso.api.repository.TransacaoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MockTransacaoRepository implements TransacaoRepository {

    private final Map<Long, Transacao> store = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    public MockTransacaoRepository() {
        seed();
    }

    private void seed() {
        List<Transacao> initial = List.of(
            Transacao.builder()
                .id(1L).valor(new BigDecimal("1500.00")).tipo(TipoTransacao.DEPOSITO)
                .direcao(DirecaoTransacao.ENTRADA).categoria(CategoriaTransacao.RECEITAS_FIXAS)
                .descricao("Salário mensal").dataTransacao(LocalDateTime.now().minusDays(2))
                .dataCadastro(LocalDateTime.now().minusDays(2)).build(),
            Transacao.builder()
                .id(2L).valor(new BigDecimal("250.00")).tipo(TipoTransacao.PIX)
                .direcao(DirecaoTransacao.SAIDA).categoria(CategoriaTransacao.CASA)
                .descricao("Pagamento aluguel").dataTransacao(LocalDateTime.now().minusDays(5))
                .dataCadastro(LocalDateTime.now().minusDays(5)).build(),
            Transacao.builder()
                .id(3L).valor(new BigDecimal("800.00")).tipo(TipoTransacao.TRANSFERENCIA)
                .direcao(DirecaoTransacao.SAIDA).categoria(CategoriaTransacao.ALIMENTACAO)
                .descricao("Compras do mês").dataTransacao(LocalDateTime.now().minusDays(10))
                .dataCadastro(LocalDateTime.now().minusDays(10)).build()
        );
        initial.forEach(t -> store.put(t.getId(), t));
        nextId.set(initial.stream().mapToLong(Transacao::getId).max().orElse(0) + 1);
    }

    @Override
    public Transacao adicionar(TransacaoInputDTO input) {
        Transacao transacao = Transacao.builder()
                .id(nextId.getAndIncrement())
                .valor(input.valor())
                .tipo(TipoTransacao.fromCodigo(input.tipo()))
                .direcao(DirecaoTransacao.fromCodigo(input.direcao()))
                .categoria(input.categoria() != null ? CategoriaTransacao.fromCodigo(input.categoria()) : null)
                .descricao(input.descricao())
                .dataTransacao(input.dataTransacao())
                .dataCadastro(LocalDateTime.now())
                .build();
        store.put(transacao.getId(), transacao);
        return transacao;
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Transacao> pesquisar(TransacaoFilterDTO filters) {
        return store.values().stream()
                .filter(t -> filters.tipo() == null || t.getTipo().getCodigo() == filters.tipo())
                .filter(t -> filters.direcao() == null || t.getDirecao().getCodigo() == filters.direcao())
                .filter(t -> filters.categoria() == null || (t.getCategoria() != null && t.getCategoria().getCodigo() == filters.categoria()))
                .filter(t -> filters.descricao() == null ||
                        (t.getDescricao() != null && t.getDescricao().toLowerCase().contains(filters.descricao().toLowerCase())))
                .filter(t -> filters.dataInicio() == null || !t.getDataTransacao().isBefore(filters.dataInicio()))
                .filter(t -> filters.dataFim() == null || !t.getDataTransacao().isAfter(filters.dataFim()))
                .sorted(Comparator.comparing(Transacao::getDataTransacao).reversed())
                .toList();
    }

    @Override
    public Optional<Transacao> atualizar(Long id, TransacaoInputDTO input) {
        Transacao existing = store.get(id);
        if (existing == null) return Optional.empty();
        Transacao updated = Transacao.builder()
                .id(id)
                .valor(input.valor())
                .tipo(TipoTransacao.fromCodigo(input.tipo()))
                .direcao(DirecaoTransacao.fromCodigo(input.direcao()))
                .categoria(input.categoria() != null ? CategoriaTransacao.fromCodigo(input.categoria()) : null)
                .descricao(input.descricao())
                .dataTransacao(input.dataTransacao())
                .dataCadastro(existing.getDataCadastro())
                .dataAtualizacao(LocalDateTime.now())
                .build();
        store.put(id, updated);
        return Optional.of(updated);
    }

    @Override
    public boolean deletar(Long id) {
        return store.remove(id) != null;
    }

    @Override
    public List<Transacao> buscarUltimasTransacoes(int limit) {
        return store.values().stream()
                .sorted(Comparator.comparing(Transacao::getDataTransacao).reversed())
                .limit(limit)
                .toList();
    }
}
