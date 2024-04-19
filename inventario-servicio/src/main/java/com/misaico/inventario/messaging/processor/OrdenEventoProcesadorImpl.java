package com.misaico.inventario.messaging.processor;

import com.misaico.common.events.inventario.InventarioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.exceptions.EventoYaProcesadoException;
import com.misaico.inventario.common.service.InventarioService;
import com.misaico.inventario.messaging.mapper.MensajeDtoMapper;
import com.misaico.common.processors.OrdenEventoProcesador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class OrdenEventoProcesadorImpl implements OrdenEventoProcesador<InventarioEvento> {

    private static final Logger log = LoggerFactory.getLogger(OrdenEventoProcesadorImpl.class);
    private final InventarioService service;
    @Override
    public Mono<InventarioEvento> handle(OrdenEvento.OrdenCreada evento) {
        return this.service.descontar(MensajeDtoMapper.toInventarioDescontadoRequest(evento))
                .map(MensajeDtoMapper::toInventarioDescontadoEvento)
                .doOnNext(e -> log.info("Pago descontado: {}", e))
                .transform(exceptionHandler(evento));
    }

    @Override
    public Mono<InventarioEvento> handle(OrdenEvento.OrdenCancelada evento) {
        return this.service.restaurar(evento.ordenId())
                .map(MensajeDtoMapper::toInventarioRestauradoEvento)
                .doOnNext(e -> log.info("Pago reembolsado: {}", e))
                .doOnError(ex -> log.error("error al reembolsar pago", ex));
    }

    @Override
    public Mono<InventarioEvento> handle(OrdenEvento.OrdenCompletada evento) {
        return Mono.empty();
    }

    private UnaryOperator<Mono<InventarioEvento>> exceptionHandler(OrdenEvento.OrdenCreada evento){
        return mono -> mono.onErrorResume(EventoYaProcesadoException.class, e -> Mono.empty())
                .onErrorResume(MensajeDtoMapper.toInventarioRechazadoEvento(evento));
    }
}
