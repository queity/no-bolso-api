package com.nobolso.api.dto.request;

import java.time.LocalDateTime;

public record TransacaoFilterDTO(
        Integer tipo,
        Integer direcao,
        Integer categoria,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim
) {}
