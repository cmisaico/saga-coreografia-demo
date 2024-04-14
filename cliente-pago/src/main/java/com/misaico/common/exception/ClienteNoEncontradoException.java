package com.misaico.common.exception;

public class ClienteNoEncontradoException extends RuntimeException {

    private static final String MENSAJE = "Cliente no encontrado";

    public ClienteNoEncontradoException() {
        super(MENSAJE);
    }

}
