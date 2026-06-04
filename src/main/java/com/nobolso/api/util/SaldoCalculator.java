package com.nobolso.api.util;

import com.nobolso.api.model.Transacao;
import com.nobolso.api.model.enums.DirecaoTransacao;

import java.math.BigDecimal;
import java.util.List;

public final class SaldoCalculator {

    private SaldoCalculator() {}

    public static BigDecimal calcular(List<Transacao> transacoes) {
        return transacoes.stream()
                .reduce(BigDecimal.ZERO, (acc, t) ->
                        t.getDirecao() == DirecaoTransacao.ENTRADA
                                ? acc.add(t.getValor())
                                : acc.subtract(t.getValor()),
                        BigDecimal::add);
    }
}
