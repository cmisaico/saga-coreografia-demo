package com.misaico.pago.messaging.processor;

import com.misaico.pago.common.service.PagoService;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.events.pago.PagoEvento;
import com.misaico.common.exceptions.EventoYaProcesadoException;
import com.misaico.pago.messaging.mapper.MensajeDtoMapper;
import com.misaico.common.processors.OrdenEventoProcesador;
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
        return this.service.reembolsar(evento.ordenId())
                .map(MensajeDtoMapper::toPagoReembolsadoEvento)
                .doOnNext(e -> log.info("Pago reembolsado: {}", e))
                .doOnError(ex -> log.error("error al reembolsar pago", ex));
    }

    @Override
    public Mono<PagoEvento> handle(OrdenEvento.OrdenCompletada evento) {
        return Mono.empty();
    }

    private UnaryOperator<Mono<PagoEvento>> exceptionHandler(OrdenEvento.OrdenCreada evento){
        return mono -> mono.onErrorResume(EventoYaProcesadoException.class, e -> Mono.empty())
                .onErrorResume(MensajeDtoMapper.toPagoRechazadoEvento(evento));
    }
}
