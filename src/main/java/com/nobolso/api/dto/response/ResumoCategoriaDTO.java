package com.nobolso.api.dto.response;

import java.math.BigDecimal;

public record ResumoCategoriaDTO(
        Integer categoria,
        BigDecimal total,
        Long quantidade
) {}
