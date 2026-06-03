package com.nobolso.api.model.converter;

import com.nobolso.api.model.enums.DirecaoTransacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DirecaoTransacaoConverter implements AttributeConverter<DirecaoTransacao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DirecaoTransacao direcao) {
        return direcao == null ? null : direcao.getCodigo();
    }

    @Override
    public DirecaoTransacao convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : DirecaoTransacao.fromCodigo(codigo);
    }
}
