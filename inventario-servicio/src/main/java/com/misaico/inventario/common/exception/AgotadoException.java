package com.misaico.inventario.common.exception;

public class AgotadoException extends RuntimeException {

    private static final String MENSAJE = "Producto agotado";

    public AgotadoException() {
        super(MENSAJE);
    }

}
