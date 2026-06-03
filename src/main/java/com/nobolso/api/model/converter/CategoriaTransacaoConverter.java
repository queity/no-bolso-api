package com.nobolso.api.model.converter;

import com.nobolso.api.model.enums.CategoriaTransacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoriaTransacaoConverter implements AttributeConverter<CategoriaTransacao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CategoriaTransacao categoria) {
        return categoria == null ? null : categoria.getCodigo();
    }

    @Override
    public CategoriaTransacao convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : CategoriaTransacao.fromCodigo(codigo);
    }
}
