package com.misaico.common.demo;

import java.util.Objects;
import java.util.function.Function;

public class MensajeHandlerImpl<I,O> implements MensajeHandler<I,O> {
    private final I input;
    private O output;

    public MensajeHandlerImpl(I input) {
        this.input = input;
    }

    @Override
    public <R> MensajeHandler<I, O> onMessage(Class<R> type, Function<R, O> function) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(function);
        if(Objects.isNull(output) && type.isInstance(input)){
            this.output = function.apply((R) input);
        }
        return this;
    }

    @Override
    public O handle() {
        return this.output;
    }
}
