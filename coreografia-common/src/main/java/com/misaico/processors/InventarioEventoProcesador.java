package com.misaico.processors;

import com.misaico.events.DomainEvent;
import com.misaico.events.inventario.InventarioEvento;
import reactor.core.publisher.Mono;

public interface InventarioEventoProcesador <R extends DomainEvent> extends EventoProcesador<InventarioEvento, R>{

    @Override
    default Mono<R> procesar(InventarioEvento event) {
        return switch (event){
            case InventarioEvento.InventarioDescontado e -> this.handle(e);
            case InventarioEvento.InventarioRechazado e -> this.handle(e);
            case InventarioEvento.InventarioRestaurado e -> this.handle(e);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    Mono<R> handle(InventarioEvento.InventarioDescontado evento);
    Mono<R> handle(InventarioEvento.InventarioRechazado evento);
    Mono<R> handle(InventarioEvento.InventarioRestaurado evento);
}
