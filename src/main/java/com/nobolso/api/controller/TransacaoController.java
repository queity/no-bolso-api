package com.nobolso.api.controller;

import com.nobolso.api.dto.request.TransacaoFilterDTO;
import com.nobolso.api.dto.request.TransacaoInputDTO;
import com.nobolso.api.dto.response.SaldoResponseDTO;
import com.nobolso.api.dto.response.TransacaoResponseDTO;
import com.nobolso.api.exception.TransacaoNaoEncontradaException;
import com.nobolso.api.mapper.TransacaoMapper;
import com.nobolso.api.model.Comprovante;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
            @Parameter(description = "Texto contido na descrição")
            @RequestParam(required = false) String descricao,
            @Parameter(description = "Data início do período (ISO 8601, ex: 2026-01-01T00:00:00)")
            @RequestParam(required = false) LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período (ISO 8601, ex: 2026-12-31T23:59:59)")
            @RequestParam(required = false) LocalDateTime dataFim) {

        TransacaoFilterDTO filter = new TransacaoFilterDTO(tipo, direcao, categoria, descricao, dataInicio, dataFim);
        return transacaoService.pesquisar(filter).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/saldo")
    @Operation(summary = "Consultar saldo", description = "Retorna o saldo calculado (entradas - saídas). Pode ser filtrado por período ou direção.")
    @ApiResponse(responseCode = "200", description = "Saldo calculado com sucesso")
    public SaldoResponseDTO buscarSaldo(
            @Parameter(description = "Direção da transação", schema = @Schema(implementation = DirecaoTransacao.class))
            @RequestParam(required = false) Integer direcao,
            @Parameter(description = "Data início do período")
            @RequestParam(required = false) LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período")
            @RequestParam(required = false) LocalDateTime dataFim) {

        TransacaoFilterDTO filter = new TransacaoFilterDTO(null, direcao, null, null, dataInicio, dataFim);
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

    @PutMapping(value = "/{id}/comprovante", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Anexar comprovante", description = "Faz upload ou substituição do comprovante da transação")
    @ApiResponse(responseCode = "200", description = "Comprovante anexado com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content(schema = @Schema(hidden = true)))
    public TransacaoResponseDTO adicionarComprovante(
            @Parameter(description = "ID da transação", required = true)
            @PathVariable Long id,
            @RequestParam("comprovante") MultipartFile comprovante) {
        return mapper.toResponse(transacaoService.adicionarComprovante(id, comprovante));
    }

    @GetMapping("/{id}/comprovante")
    @Operation(summary = "Download do comprovante", description = "Retorna o arquivo de comprovante anexado à transação")
    @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação ou comprovante não encontrado", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<byte[]> downloadComprovante(
            @Parameter(description = "ID da transação", required = true)
            @PathVariable Long id) {
        Transacao transacao = transacaoService.buscarPorId(id);
        Comprovante comprovante = transacao.getComprovante();
        if (comprovante == null) {
            throw new TransacaoNaoEncontradaException("Transação " + id + " não possui comprovante");
        }
        String contentType = Objects.requireNonNullElse(
                comprovante.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String nome = Objects.requireNonNullElse(comprovante.getNome(), "comprovante");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nome + "\"")
                .body(comprovante.getBytes());
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
