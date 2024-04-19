package com.misaico.common.processors;

import com.misaico.common.events.DomainEvent;
import com.misaico.common.events.orden.OrdenEvento;
import reactor.core.publisher.Mono;

public interface OrdenEventoProcesador <R extends DomainEvent> extends EventoProcesador<OrdenEvento, R>{

    @Override
    default Mono<R> procesar(OrdenEvento event) {
        return switch (event){
            case OrdenEvento.OrdenCreada e -> this.handle(e);
            case OrdenEvento.OrdenCancelada e -> this.handle(e);
            case OrdenEvento.OrdenCompletada e -> this.handle(e);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    Mono<R> handle(OrdenEvento.OrdenCreada evento);
    Mono<R> handle(OrdenEvento.OrdenCancelada evento);
    Mono<R> handle(OrdenEvento.OrdenCompletada evento);
}
