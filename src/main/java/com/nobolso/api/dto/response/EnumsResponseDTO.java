package com.nobolso.api.dto.response;

import java.util.List;

public record EnumsResponseDTO(
        List<EnumOptionDTO> tipos,
        List<EnumOptionDTO> direcoes,
        List<EnumOptionDTO> categorias
) {
    public record EnumOptionDTO(int codigo, String descricao) {}
}
