package com.nobolso.api.exception;

public class TransacaoNaoEncontradaException extends RuntimeException {
    public TransacaoNaoEncontradaException(Long id) {
        super("Transação não encontrada: " + id);
    }
}
