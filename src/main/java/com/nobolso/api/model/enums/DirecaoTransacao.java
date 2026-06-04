package com.nobolso.api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DirecaoTransacao implements ICodigoEnum {
    ENTRADA(1, "Entrada"),
    SAIDA(2, "Saída");

    private final int codigo;
    private final String descricao;

    DirecaoTransacao(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static DirecaoTransacao fromCodigo(int codigo) {
        for (DirecaoTransacao direcao : values()) {
            if (direcao.codigo == codigo) return direcao;
        }
        throw new IllegalArgumentException("DirecaoTransacao inválida: " + codigo);
    }
}
