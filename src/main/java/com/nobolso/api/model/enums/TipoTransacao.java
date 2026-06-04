package com.nobolso.api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoTransacao implements ICodigoEnum {
    PIX(1, "Pix"),
    DEPOSITO(2, "Depósito"),
    TRANSFERENCIA(3, "Transferência"),
    SAQUE(4, "Saque"),
    OUTROS(5, "Outros");

    private final int codigo;
    private final String descricao;

    TipoTransacao(int codigo, String descricao) {
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
    public static TipoTransacao fromCodigo(int codigo) {
        for (TipoTransacao tipo : values()) {
            if (tipo.codigo == codigo) return tipo;
        }
        throw new IllegalArgumentException("TipoTransacao inválido: " + codigo);
    }
}
