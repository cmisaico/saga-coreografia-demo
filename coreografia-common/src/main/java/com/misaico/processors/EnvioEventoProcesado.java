package com.misaico.processors;

import com.misaico.events.DomainEvent;
import com.misaico.events.envio.EnvioEvento;
import reactor.core.publisher.Mono;

public interface EnvioEventoProcesado <R extends DomainEvent> extends EventoProcesador<EnvioEvento, R>{

    @Override
    default Mono<R> procesar(EnvioEvento event) {
        return switch (event){
            case EnvioEvento.EnvioProgramado e -> this.handle(e);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    Mono<R> handle(EnvioEvento.EnvioProgramado evento);

}
