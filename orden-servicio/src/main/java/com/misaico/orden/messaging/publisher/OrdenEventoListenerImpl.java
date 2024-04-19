package com.misaico.orden.messaging.publisher;

import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.orden.common.dto.OrdenCompraDto;
import com.misaico.orden.common.service.OrdenEventoListener;
import com.misaico.orden.messaging.mapper.OrdenEventoMapper;
import com.misaico.common.publisher.EventoPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RequiredArgsConstructor
public class OrdenEventoListenerImpl implements OrdenEventoListener, EventoPublisher<OrdenEvento> {

    private final Sinks.Many<OrdenEvento> sink;
    private final Flux<OrdenEvento> flux;

    @Override
    public Flux<OrdenEvento> publish() {
        return this.flux;
    }

    @Override
    public void emitirOrdenCreado(OrdenCompraDto dto) {
        var evento = OrdenEventoMapper.toOrdenCreatedEvento(dto);
        this.sink.emitNext(
                evento,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }


}
