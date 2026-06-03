package com.nobolso.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados para criação ou atualização de uma transação")
public record TransacaoInputDTO(
        @Schema(description = "Valor da transação", example = "150.00")
        @NotNull @Positive BigDecimal valor,

        @Schema(description = "Tipo: 1=Pix, 2=Depósito, 3=Transferência, 4=Saque, 5=Outros", example = "1")
        @NotNull Integer tipo,

        @Schema(description = "Direção: 1=Entrada, 2=Saída", example = "2")
        @NotNull Integer direcao,

        @Schema(description = "Categoria: 1=Alimentação, 2=Lazer, 3=Assinatura, 4=Casa, 5=Educação, 6=Receitas Fixas, 7=Outros", example = "1")
        Integer categoria,

        @Schema(description = "Descrição livre da transação", example = "Almoço no restaurante")
        String descricao,

        @Schema(description = "Data da transação (ISO 8601)", example = "2026-06-01T12:00:00")
        @NotNull LocalDateTime dataTransacao
) {}
