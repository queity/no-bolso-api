package com.nobolso.api.model.enums;

import java.util.Arrays;

public interface ICodigoEnum {
    int getCodigo();
    String getDescricao();

    static <E extends Enum<E> & ICodigoEnum> E fromCodigo(Class<E> enumClass, int codigo) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Código inválido " + codigo + " para " + enumClass.getSimpleName()));
    }
}
