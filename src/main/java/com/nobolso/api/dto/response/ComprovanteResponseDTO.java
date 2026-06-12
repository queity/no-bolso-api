package com.nobolso.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do comprovante enviado")
public record ComprovanteResponseDTO(
        @Schema(description = "Identificador do comprovante", example = "1")
        Long id,

        @Schema(description = "Nome original do arquivo", example = "recibo.pdf")
        String nome,

        @Schema(description = "Tipo MIME do arquivo", example = "application/pdf")
        String contentType
) {}
