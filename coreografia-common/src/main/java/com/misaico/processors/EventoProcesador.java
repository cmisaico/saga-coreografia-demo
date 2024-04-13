package com.misaico.processors;


import com.misaico.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventoProcesador <T extends DomainEvent, R extends DomainEvent> {
    Mono<R> procesar(T event);
}
