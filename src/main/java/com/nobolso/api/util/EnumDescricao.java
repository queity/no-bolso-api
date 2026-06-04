package com.nobolso.api.util;

import com.nobolso.api.model.enums.ICodigoEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class EnumDescricao {

    private EnumDescricao() {}

    public static <E extends Enum<E> & ICodigoEnum> String of(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> e.getCodigo() + "=" + e.getDescricao())
                .collect(Collectors.joining(", "));
    }
}
