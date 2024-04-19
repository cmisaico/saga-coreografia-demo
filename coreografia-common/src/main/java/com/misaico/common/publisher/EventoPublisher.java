package com.misaico.common.publisher;

import com.misaico.common.events.DomainEvent;
import reactor.core.publisher.Flux;

public interface EventoPublisher <T extends DomainEvent> {
    Flux<T> publish();
}
