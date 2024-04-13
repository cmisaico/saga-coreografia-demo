package com.misaico.exceptions;

public class EventoYaProcesadoException extends RuntimeException {

    private static final String MENSAJE = "El evento ya ha sido procesado";

    public EventoYaProcesadoException() {
        super(MENSAJE);
    }
}
