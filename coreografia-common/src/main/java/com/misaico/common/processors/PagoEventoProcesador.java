package com.misaico.common.processors;

import com.misaico.common.events.DomainEvent;
import com.misaico.common.events.pago.PagoEvento;
import reactor.core.publisher.Mono;

public interface PagoEventoProcesador <R extends DomainEvent> extends EventoProcesador<PagoEvento, R>{

    @Override
    default Mono<R> procesar(PagoEvento event) {
        return switch (event){
            case PagoEvento.PagoDescontado e -> this.handle(e);
            case PagoEvento.PagoRechazado e -> this.handle(e);
            case PagoEvento.PagoReembolsado e -> this.handle(e);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    Mono<R> handle(PagoEvento.PagoDescontado evento);
    Mono<R> handle(PagoEvento.PagoRechazado evento);
    Mono<R> handle(PagoEvento.PagoReembolsado evento);
}