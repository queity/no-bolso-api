package com.nobolso.api.model.converter;

import com.nobolso.api.model.enums.TipoTransacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoTransacaoConverter implements AttributeConverter<TipoTransacao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoTransacao tipo) {
        return tipo == null ? null : tipo.getCodigo();
    }

    @Override
    public TipoTransacao convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : TipoTransacao.fromCodigo(codigo);
    }
}
