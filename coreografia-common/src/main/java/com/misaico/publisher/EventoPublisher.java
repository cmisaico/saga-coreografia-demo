package com.misaico.publisher;

import com.misaico.events.DomainEvent;
import reactor.core.publisher.Flux;

public interface EventoPublisher <T extends DomainEvent> {
    Flux<T> publish();
}
