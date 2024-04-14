package com.misaico.messaging.processor;

import com.misaico.common.service.PagoService;
import com.misaico.events.orden.OrdenEvento;
import com.misaico.events.pago.PagoEvento;
import com.misaico.exceptions.EventoYaProcesadoException;
import com.misaico.messaging.mapper.MensajeDtoMapper;
import com.misaico.processors.OrdenEventoProcesador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class OrdenEventoProcesadorImpl implements OrdenEventoProcesador<PagoEvento> {

    private static final Logger log = LoggerFactory.getLogger(OrdenEventoProcesadorImpl.class);
    private final PagoService service;
    @Override
    public Mono<PagoEvento> handle(OrdenEvento.OrdenCreada evento) {
        return this.service.procesar(MensajeDtoMapper.toPagoProcessRequest(evento))
                .map(MensajeDtoMapper::toPagoDescontadoEvento)
                .doOnNext(e -> log.info("Pago descontado: {}", e))
                .transform(exceptionHandler(evento));
    }

    @Override
    public Mono<PagoEvento> handle(OrdenEvento.OrdenCancelada evento) {
        return null;
    }

    @Override
    public Mono<PagoEvento> handle(OrdenEvento.OrdenCompletada evento) {
        return null;
    }

    private UnaryOperator<Mono<PagoEvento>> exceptionHandler(OrdenEvento.OrdenCreada evento){
        return mono -> mono.onErrorResume(EventoYaProcesadoException.class, e -> Mono.empty())
                .onErrorResume(MensajeDtoMapper.toPagoRechazadoEvento(evento));
    }
}
