package com.misaico.common.processors;


import com.misaico.common.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventoProcesador <T extends DomainEvent, R extends DomainEvent> {
    Mono<R> procesar(T event);
}
