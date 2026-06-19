package com.nobolso.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta paginada")
public record PageResponseDTO<T>(
        @Schema(description = "Itens da página atual")
        List<T> content,

        @Schema(description = "Número da página atual", example = "1")
        int page,

        @Schema(description = "Tamanho da página", example = "10")
        int size,

        @Schema(description = "Total de itens encontrados", example = "42")
        long totalElements,

        @Schema(description = "Total de páginas", example = "5")
        int totalPages
) {}
