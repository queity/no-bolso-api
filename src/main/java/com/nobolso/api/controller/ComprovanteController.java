package com.nobolso.api.controller;

import com.nobolso.api.dto.response.ComprovanteResponseDTO;
import com.nobolso.api.model.Comprovante;
import com.nobolso.api.service.ComprovanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/comprovantes")
@Tag(name = "Comprovantes", description = "Upload de comprovantes para anexo em transações")
public class ComprovanteController {

    private final ComprovanteService comprovanteService;

    public ComprovanteController(ComprovanteService comprovanteService) {
        this.comprovanteService = comprovanteService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload de comprovante", description = "Envia um arquivo e retorna o ID para uso no cadastro de transação")
    @ApiResponse(responseCode = "201", description = "Comprovante salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido")
    public ResponseEntity<ComprovanteResponseDTO> upload(
            @RequestParam("arquivo") MultipartFile arquivo) {
        Comprovante comprovante = comprovanteService.salvar(arquivo);
        ComprovanteResponseDTO response = new ComprovanteResponseDTO(
                comprovante.getId(),
                comprovante.getNome(),
                comprovante.getContentType());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(comprovante.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Download do comprovante", description = "Retorna o arquivo de comprovante pelo ID")
    @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado", content = @Content(schema = @Schema(hidden = true)))
    @SuppressWarnings("null")
    public ResponseEntity<byte[]> download(
            @Parameter(description = "ID do comprovante", required = true)
            @PathVariable Long id) {
        Comprovante comprovante = comprovanteService.buscarPorId(id);
        String ct = comprovante.getContentType();
        MediaType mediaType = ct != null ? MediaType.parseMediaType(ct) : MediaType.APPLICATION_OCTET_STREAM;
        String nome = Objects.requireNonNullElse(comprovante.getNome(), "comprovante");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nome + "\"")
                .body(comprovante.getBytes());
    }
}
