package com.nobolso.api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoriaTransacao implements ICodigoEnum {
    ALIMENTACAO(1, "Alimentação"),
    LAZER(2, "Lazer"),
    ASSINATURA(3, "Assinatura"),
    CASA(4, "Casa"),
    EDUCACAO(5, "Educação"),
    RECEITAS_FIXAS(6, "Receitas Fixas"),
    OUTROS(7, "Outros");

    private final int codigo;
    private final String descricao;

    CategoriaTransacao(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public int getCodigo() { return codigo; }

    public String getDescricao() { return descricao; }

    @JsonCreator
    public static CategoriaTransacao fromCodigo(int codigo) {
        return ICodigoEnum.fromCodigo(CategoriaTransacao.class, codigo);
    }
}
