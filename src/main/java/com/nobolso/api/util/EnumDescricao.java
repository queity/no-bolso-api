package com.nobolso.api.util;

import com.nobolso.api.model.enums.ICodigoEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class EnumDescricao {

    private EnumDescricao() {}

    public static String of(Class<? extends ICodigoEnum> enumClass) {
        if (!enumClass.isEnum()) throw new IllegalArgumentException("Classe não é um enum: " + enumClass);
        return Arrays.stream((ICodigoEnum[]) enumClass.getEnumConstants())
                .map(e -> e.getCodigo() + "=" + e.getDescricao())
                .collect(Collectors.joining(", "));
    }
}
