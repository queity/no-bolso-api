package com.nobolso.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
        Long id,
        BigDecimal valor,
        EnumDTO tipo,
        EnumDTO direcao,
        EnumDTO categoria,
        String descricao,
        LocalDateTime dataTransacao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {
    public record EnumDTO(int codigo, String descricao) {}
}
