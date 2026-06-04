package com.nobolso.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Saldo calculado a partir das transações filtradas")
public record SaldoResponseDTO(
        @Schema(description = "Saldo total (entradas - saídas)", example = "450.00")
        BigDecimal saldo
) {}
