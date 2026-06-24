package com.nobolso.api.controller;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.dto.response.PageResponseDTO;
import com.nobolso.api.dto.response.ResumoCategoriaDTO;
import com.nobolso.api.dto.response.SaldoResponseDTO;
import com.nobolso.api.dto.response.TransacaoResponseDTO;
import com.nobolso.api.mapper.TransacaoMapper;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import com.nobolso.api.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "Gerenciamento de transações financeiras")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final TransacaoMapper mapper;

    public TransacaoController(TransacaoService transacaoService, TransacaoMapper mapper) {
        this.transacaoService = transacaoService;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Pesquisar transações", description = "Retorna a lista de transações com filtros opcionais.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<TransacaoResponseDTO> pesquisar(
            @Parameter(description = "Tipo da transação", schema = @Schema(implementation = TipoTransacao.class))
            @RequestParam(required = false) Integer tipo,
            @Parameter(description = "Direção da transação", schema = @Schema(implementation = DirecaoTransacao.class))
            @RequestParam(required = false) Integer direcao,
            @Parameter(description = "Categoria da transação", schema = @Schema(implementation = CategoriaTransacao.class))
            @RequestParam(required = false) Integer categoria,
            @Parameter(description = "Filtrar apenas transações sem categoria")
            @RequestParam(required = false) Boolean semCategoria,
            @Parameter(description = "Texto contido na descrição")
            @RequestParam(required = false) String descricao,
            @Parameter(description = "Data início do período (ISO 8601, ex: 2026-01-01T00:00:00)")
            @RequestParam(required = false) LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período (ISO 8601, ex: 2026-12-31T23:59:59)")
            @RequestParam(required = false) LocalDateTime dataFim) {

        TransacaoFilterDTO filter = new TransacaoFilterDTO(tipo, direcao, categoria, semCategoria, descricao, dataInicio, dataFim, null);
        return transacaoService.pesquisar(filter).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/paginado")
    @Operation(summary = "Pesquisar transações paginado", description = "Retorna transações paginadas.")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PageResponseDTO<TransacaoResponseDTO> pesquisarPaginado(
            @Parameter(description = "Tipo da transação", schema = @Schema(implementation = TipoTransacao.class))
            @RequestParam(required = false) Integer tipo,
            @Parameter(description = "Direção da transação", schema = @Schema(implementation = DirecaoTransacao.class))
            @RequestParam(required = false) Integer direcao,
            @Parameter(description = "Categoria da transação", schema = @Schema(implementation = CategoriaTransacao.class))
            @RequestParam(required = false) Integer categoria,
            @Parameter(description = "Filtrar apenas transações sem categoria")
            @RequestParam(required = false) Boolean semCategoria,
            @Parameter(description = "Texto contido na descrição")
            @RequestParam(required = false) String descricao,
            @Parameter(description = "Data início do período (ISO 8601)")
            @RequestParam(required = false) LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período (ISO 8601)")
            @RequestParam(required = false) LocalDateTime dataFim,
            @Parameter(description = "Número da página (base 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Tamanho da página (máx. 100)", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        TransacaoFilterDTO filter = new TransacaoFilterDTO(tipo, direcao, categoria, semCategoria, descricao, dataInicio, dataFim, null);
        PageResponseDTO<Transacao> resultado = transacaoService.pesquisarPaginado(filter, page, size);
        List<TransacaoResponseDTO> content = resultado.content().stream().map(mapper::toResponse).toList();
        return new PageResponseDTO<>(content, resultado.page(), resultado.size(), resultado.totalElements(), resultado.totalPages());
    }

    @GetMapping("/saldo")
    @Operation(summary = "Consultar saldo", description = "Retorna o saldo calculado (entradas - saídas). Pode ser filtrado por período, direção e categoria.")
    @ApiResponse(responseCode = "200", description = "Saldo calculado com sucesso")
    public SaldoResponseDTO buscarSaldo(
            @Parameter(description = "Direção da transação", schema = @Schema(implementation = DirecaoTransacao.class))
            @RequestParam(required = false) Integer direcao,
            @Parameter(description = "Categoria da transação", schema = @Schema(implementation = CategoriaTransacao.class))
            @RequestParam(required = false) Integer categoria,
            @Parameter(description = "Filtrar apenas transações sem categoria")
            @RequestParam(required = false) Boolean semCategoria,
            @Parameter(description = "Data início do período")
            @RequestParam(required = false) LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período")
            @RequestParam(required = false) LocalDateTime dataFim) {

        TransacaoFilterDTO filter = new TransacaoFilterDTO(null, direcao, categoria, semCategoria, null, dataInicio, dataFim, null);
        return new SaldoResponseDTO(transacaoService.buscarSaldo(filter));
    }

    @GetMapping("/recentes")
    @Operation(summary = "Últimas transações", description = "Retorna as N transações mais recentes ordenadas por data")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<TransacaoResponseDTO> buscarRecentes(
            @Parameter(description = "Quantidade de transações a retornar (padrão: 5)")
            @RequestParam(defaultValue = "5") int limit) {
        return transacaoService.buscarUltimasTransacoes(limit).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/resumo/categorias")
    @Operation(summary = "Resumo por categoria", description = "Retorna total e quantidade de transações agrupados por categoria")
    @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    public List<ResumoCategoriaDTO> resumoPorCategoria(
            @Parameter(description = "Direção da transação", schema = @Schema(implementation = DirecaoTransacao.class))
            @RequestParam(required = false) Integer direcao,
            @Parameter(description = "Data início do período")
            @RequestParam(required = false) LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período")
            @RequestParam(required = false) LocalDateTime dataFim) {

        TransacaoFilterDTO filter = new TransacaoFilterDTO(null, direcao, null, null, null, dataInicio, dataFim, null);
        return transacaoService.resumoPorCategoria(filter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    @ApiResponse(responseCode = "200", description = "Transação encontrada")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content(schema = @Schema(hidden = true)))
    public TransacaoResponseDTO buscarPorId(
            @Parameter(description = "ID da transação", required = true)
            @PathVariable Long id) {
        return mapper.toResponse(transacaoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar transação", description = "Cria uma nova transação financeira")
    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<TransacaoResponseDTO> adicionar(@Valid @RequestBody TransacaoInputDTO dto) {
        TransacaoResponseDTO response = mapper.toResponse(transacaoService.adicionar(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação", description = "Substitui os dados de uma transação existente")
    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content(schema = @Schema(hidden = true)))
    public TransacaoResponseDTO atualizar(
            @Parameter(description = "ID da transação", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TransacaoInputDTO dto) {
        return mapper.toResponse(transacaoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transação")
    @ApiResponse(responseCode = "204", description = "Transação removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da transação", required = true)
            @PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
