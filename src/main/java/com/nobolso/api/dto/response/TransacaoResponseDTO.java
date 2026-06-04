package com.nobolso.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados de uma transação")
public record TransacaoResponseDTO(
        @Schema(description = "Identificador único da transação", example = "1")
        Long id,

        @Schema(description = "Valor da transação", example = "150.00")
        BigDecimal valor,

        @Schema(description = "Tipo da transação")
        EnumDTO tipo,

        @Schema(description = "Direção da transação")
        EnumDTO direcao,

        @Schema(description = "Categoria da transação")
        EnumDTO categoria,

        @Schema(description = "Descrição livre da transação", example = "Almoço no restaurante")
        String descricao,

        @Schema(description = "Data em que a transação ocorreu", example = "2026-06-01T12:00:00")
        LocalDateTime dataTransacao,

        @Schema(description = "Data em que o registro foi criado", example = "2026-06-01T12:00:00")
        LocalDateTime dataCadastro,

        @Schema(description = "Data da última atualização do registro", example = "2026-06-01T12:00:00")
        LocalDateTime dataAtualizacao
) {
        @Schema(description = "Representação de um enum com código e descrição")
        public record EnumDTO(
                @Schema(description = "Código numérico", example = "1") int codigo,
                @Schema(description = "Descrição legível", example = "Pix") String descricao
        ) {}
}
