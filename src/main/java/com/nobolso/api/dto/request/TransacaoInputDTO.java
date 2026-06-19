package com.nobolso.api.dto.request;

import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados para criação ou atualização de uma transação")
public record TransacaoInputDTO(
        @Schema(description = "Valor da transação (entre 0.01 e 999999999.99)", example = "150.00")
        @NotNull
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        @DecimalMax(value = "999999999.99", message = "O valor excede o limite permitido")
        BigDecimal valor,

        @Schema(description = "Tipo da transação", implementation = TipoTransacao.class, example = "1")
        @NotNull(message = "O tipo é obrigatório")
        Integer tipo,

        @Schema(description = "Direção da transação", implementation = DirecaoTransacao.class, example = "2")
        @NotNull(message = "A direção é obrigatória")
        Integer direcao,

        @Schema(description = "Categoria da transação", implementation = CategoriaTransacao.class, example = "1")
        Integer categoria,

        @Schema(description = "Descrição livre da transação", example = "Almoço no restaurante")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @Schema(description = "Data da transação (ISO 8601)", example = "2026-06-01T12:00:00")
        @NotNull(message = "A data da transação é obrigatória")
        @PastOrPresent(message = "A data da transação não pode ser futura")
        LocalDateTime dataTransacao,

        @Schema(description = "ID do comprovante previamente enviado", example = "1")
        Long comprovanteId
) {}
