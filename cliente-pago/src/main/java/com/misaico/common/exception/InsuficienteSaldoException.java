package com.misaico.common.exception;

public class InsuficienteSaldoException extends RuntimeException {

    private static final String MENSAJE = "Saldo insuficiente";

    public InsuficienteSaldoException() {
        super(MENSAJE);
    }
}
