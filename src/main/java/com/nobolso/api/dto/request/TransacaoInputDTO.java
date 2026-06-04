package com.nobolso.api.dto.request;

import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados para criação ou atualização de uma transação")
public record TransacaoInputDTO(
        @Schema(description = "Valor da transação", example = "150.00")
        @NotNull @Positive BigDecimal valor,

        @Schema(description = "Tipo da transação", implementation = TipoTransacao.class, example = "1")
        @NotNull Integer tipo,

        @Schema(description = "Direção da transação", implementation = DirecaoTransacao.class, example = "2")
        @NotNull Integer direcao,

        @Schema(description = "Categoria da transação", implementation = CategoriaTransacao.class, example = "1")
        Integer categoria,

        @Schema(description = "Descrição livre da transação", example = "Almoço no restaurante")
        String descricao,

        @Schema(description = "Data da transação (ISO 8601)", example = "2026-06-01T12:00:00")
        @NotNull LocalDateTime dataTransacao
) {}
