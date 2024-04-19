package com.misaico.common.demo;

import java.util.function.Function;

public interface MensajeHandler<I, O> {
    <R> MensajeHandler<I, O> onMessage(Class<R> type, Function<R, O> function);

    O handle();

    static <I, O> MensajeHandler<I, O> create(I input){
        return new MensajeHandlerImpl<>(input);
    }
}
