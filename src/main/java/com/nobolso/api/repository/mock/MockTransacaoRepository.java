package com.nobolso.api.repository.mock;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.dto.response.PageResponseDTO;
import com.nobolso.api.dto.response.ResumoCategoriaDTO;
import com.nobolso.api.model.Comprovante;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.model.Usuario;
import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import com.nobolso.api.repository.TransacaoRepository;
import com.nobolso.api.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Profile("dev")
@Repository
public class MockTransacaoRepository implements TransacaoRepository {

    private final Map<Long, Transacao> store = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    public MockTransacaoRepository(UsuarioRepository usuarioRepository) {
        // Aguarda o usuário dev estar disponível para associar as transações seed
        usuarioRepository.buscarPorEmail("dev@nobolso.com").ifPresent(this::seed);
    }

    private void seed(Usuario dev) {
        LocalDateTime now = LocalDateTime.now();

        List<Transacao> initial = List.of(
            tx(1,  "1500.00", TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_FIXAS,    "Salário mensal",             now.minusDays(1),  dev),
            tx(2,  "320.00",  TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.CASA,              "Aluguel",                    now.minusDays(2),  dev),
            tx(3,  "85.50",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.ALIMENTACAO,       "Supermercado",               now.minusDays(3),  dev),
            tx(4,  "200.00",  TipoTransacao.TRANSFERENCIA, DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_VARIAVEIS,"Freelance design",           now.minusDays(4),  dev),
            tx(5,  "49.90",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.LAZER,             "Streaming de música",        now.minusDays(5),  dev),
            tx(6,  "130.00",  TipoTransacao.SAQUE,         DirecaoTransacao.SAIDA,   CategoriaTransacao.SAUDE,             "Farmácia",                   now.minusDays(8),  dev),
            tx(7,  "60.00",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.TRANSPORTE,        "Abastecimento",              now.minusDays(10), dev),
            tx(8,  "500.00",  TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, null,                                 "Reembolso empresa",          now.minusDays(12), dev),
            tx(9,  "75.00",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.ALIMENTACAO,       "Restaurante almoço",         now.minusDays(14), dev),
            tx(10, "25.00",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   null,                                 "Gorjeta",                    now.minusDays(15), dev),
            tx(11, "1500.00", TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_FIXAS,    "Salário mensal",             now.minusMonths(1).withDayOfMonth(5),  dev),
            tx(12, "320.00",  TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.CASA,              "Aluguel",                    now.minusMonths(1).withDayOfMonth(8),  dev),
            tx(13, "150.00",  TipoTransacao.TRANSFERENCIA, DirecaoTransacao.SAIDA,   CategoriaTransacao.EDUCACAO,          "Curso online",               now.minusMonths(1).withDayOfMonth(12), dev),
            tx(14, "40.00",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.LAZER,             "Cinema",                     now.minusMonths(1).withDayOfMonth(18), dev),
            tx(15, "95.00",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.SAUDE,             "Consulta médica",            now.minusMonths(1).withDayOfMonth(22), dev),
            tx(16, "1500.00", TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_FIXAS,    "Salário mensal",             now.minusMonths(2).withDayOfMonth(5),  dev),
            tx(17, "320.00",  TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.CASA,              "Aluguel",                    now.minusMonths(2).withDayOfMonth(8),  dev),
            tx(18, "220.00",  TipoTransacao.SAQUE,         DirecaoTransacao.SAIDA,   CategoriaTransacao.ALIMENTACAO,       "Feira do mês",               now.minusMonths(2).withDayOfMonth(15), dev),
            tx(19, "300.00",  TipoTransacao.TRANSFERENCIA, DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_VARIAVEIS,"Venda produto usado",        now.minusMonths(2).withDayOfMonth(20), dev),
            tx(20, "1500.00", TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_FIXAS,    "Salário mensal",             now.minusMonths(6).withDayOfMonth(5),  dev),
            tx(21, "1200.00", TipoTransacao.TRANSFERENCIA, DirecaoTransacao.SAIDA,   CategoriaTransacao.CASA,              "Parcela apartamento",        now.minusMonths(6).withDayOfMonth(10), dev),
            tx(22, "80.00",   TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.LAZER,             "Show de música",             now.minusMonths(6).withDayOfMonth(25), dev),
            tx(23, "1400.00", TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_FIXAS,    "Salário mensal",             now.minusYears(1).withMonth(3).withDayOfMonth(5),   dev),
            tx(24, "320.00",  TipoTransacao.PIX,           DirecaoTransacao.SAIDA,   CategoriaTransacao.CASA,              "Aluguel",                    now.minusYears(1).withMonth(3).withDayOfMonth(8),   dev),
            tx(25, "450.00",  TipoTransacao.TRANSFERENCIA, DirecaoTransacao.SAIDA,   CategoriaTransacao.VIAGEM,            "Passagem aérea",             now.minusYears(1).withMonth(7).withDayOfMonth(14),  dev),
            tx(26, "900.00",  TipoTransacao.DEPOSITO,      DirecaoTransacao.ENTRADA, CategoriaTransacao.RECEITAS_VARIAVEIS,"Décimo terceiro",            now.minusYears(1).withMonth(12).withDayOfMonth(20), dev)
        );

        initial.forEach(t -> store.put(t.getId(), t));
        nextId.set(initial.stream().mapToLong(Transacao::getId).max().orElse(0) + 1);
    }

    private Transacao tx(long id, String valor, TipoTransacao tipo, DirecaoTransacao direcao,
                         CategoriaTransacao categoria, String descricao, LocalDateTime data, Usuario usuario) {
        return Transacao.builder()
                .id(id).valor(new BigDecimal(valor)).tipo(tipo).direcao(direcao)
                .categoria(categoria).descricao(descricao).dataTransacao(data)
                .dataCadastro(data).usuario(usuario)
                .build();
    }

    @Override
    public Transacao adicionar(TransacaoInputDTO input, Comprovante comprovante, Usuario usuario) {
        Transacao t = Transacao.builder()
                .id(nextId.getAndIncrement())
                .valor(input.valor())
                .tipo(TipoTransacao.fromCodigo(input.tipo()))
                .direcao(DirecaoTransacao.fromCodigo(input.direcao()))
                .categoria(input.categoria() != null ? CategoriaTransacao.fromCodigo(input.categoria()) : null)
                .descricao(input.descricao())
                .dataTransacao(input.dataTransacao())
                .dataCadastro(LocalDateTime.now())
                .comprovante(comprovante)
                .usuario(usuario)
                .build();
        store.put(t.getId(), t);
        return t;
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id, Long usuarioId) {
        return Optional.ofNullable(store.get(id))
                .filter(t -> t.getUsuario().getId().equals(usuarioId));
    }

    @Override
    public List<Transacao> pesquisar(TransacaoFilterDTO filters) {
        return store.values().stream()
                .filter(t -> filters.usuarioId() == null || t.getUsuario().getId().equals(filters.usuarioId()))
                .filter(t -> filters.tipo() == null || t.getTipo().getCodigo() == filters.tipo())
                .filter(t -> filters.direcao() == null || t.getDirecao().getCodigo() == filters.direcao())
                .filter(t -> {
                    if (Boolean.TRUE.equals(filters.semCategoria())) return t.getCategoria() == null;
                    if (filters.categoria() != null) return t.getCategoria() != null && t.getCategoria().getCodigo() == filters.categoria();
                    return true;
                })
                .filter(t -> filters.descricao() == null ||
                        (t.getDescricao() != null && t.getDescricao().toLowerCase().contains(filters.descricao().toLowerCase())))
                .filter(t -> filters.dataInicio() == null || !t.getDataTransacao().isBefore(filters.dataInicio()))
                .filter(t -> filters.dataFim() == null || !t.getDataTransacao().isAfter(filters.dataFim()))
                .sorted(Comparator.comparing(Transacao::getDataTransacao).reversed())
                .toList();
    }

    @Override
    public PageResponseDTO<Transacao> pesquisarPaginado(TransacaoFilterDTO filters, int page, int size) {
        List<Transacao> todos = pesquisar(filters);
        long total = todos.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;
        int fromIndex = Math.min((page - 1) * size, (int) total);
        int toIndex = Math.min(fromIndex + size, (int) total);
        return new PageResponseDTO<>(todos.subList(fromIndex, toIndex), page, size, total, totalPages);
    }

    @Override
    public Optional<Transacao> atualizar(Long id, TransacaoInputDTO input, Comprovante comprovante, Long usuarioId) {
        Transacao existing = store.get(id);
        if (existing == null || !existing.getUsuario().getId().equals(usuarioId)) return Optional.empty();
        Transacao updated = Transacao.builder()
                .id(id).valor(input.valor())
                .tipo(TipoTransacao.fromCodigo(input.tipo()))
                .direcao(DirecaoTransacao.fromCodigo(input.direcao()))
                .categoria(input.categoria() != null ? CategoriaTransacao.fromCodigo(input.categoria()) : null)
                .descricao(input.descricao())
                .dataTransacao(input.dataTransacao())
                .dataCadastro(existing.getDataCadastro())
                .dataAtualizacao(LocalDateTime.now())
                .comprovante(comprovante)
                .usuario(existing.getUsuario())
                .build();
        store.put(id, updated);
        return Optional.of(updated);
    }

    @Override
    public boolean deletar(Long id, Long usuarioId) {
        Transacao t = store.get(id);
        if (t == null || !t.getUsuario().getId().equals(usuarioId)) return false;
        store.remove(id);
        return true;
    }

    @Override
    public List<Transacao> buscarUltimasTransacoes(int limit, Long usuarioId) {
        return store.values().stream()
                .filter(t -> t.getUsuario().getId().equals(usuarioId))
                .sorted(Comparator.comparing(Transacao::getDataTransacao).reversed())
                .limit(limit)
                .toList();
    }

    @Override
    public List<ResumoCategoriaDTO> resumoPorCategoria(TransacaoFilterDTO filters) {
        return pesquisar(filters).stream()
                .filter(t -> t.getCategoria() != null)
                .collect(Collectors.groupingBy(t -> t.getCategoria().getCodigo()))
                .entrySet().stream()
                .map(e -> new ResumoCategoriaDTO(
                        e.getKey(),
                        e.getValue().stream().map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add),
                        (long) e.getValue().size()
                ))
                .sorted(Comparator.comparing(ResumoCategoriaDTO::total).reversed())
                .toList();
    }
}
