package com.misaico.orden.messaging.processor;

import com.misaico.common.events.inventario.InventarioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.orden.common.service.OrdenCumplimientoService;
import com.misaico.orden.common.service.inventario.InventarioComponenteEstadoListener;
import com.misaico.orden.messaging.mapper.InventarioEventoMapper;
import com.misaico.orden.messaging.mapper.OrdenEventoMapper;
import com.misaico.common.processors.InventarioEventoProcesador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class InventarioEventoProcesadorImpl implements InventarioEventoProcesador<OrdenEvento> {

    private static final Logger log = LoggerFactory.getLogger(InventarioEventoProcesadorImpl.class);
    private final OrdenCumplimientoService ordenCumplimientoService;
    private final InventarioComponenteEstadoListener estadoListener;

    @Override
    public Mono<OrdenEvento> handle(InventarioEvento.InventarioDescontado evento) {
        var dto = InventarioEventoMapper.toDto(evento);
        return this.estadoListener.onSuccess(dto)
                .then(this.ordenCumplimientoService.completar(evento.ordenId()))
                .map(OrdenEventoMapper::toOrdenCompletadoEvento);
    }

    @Override
    public Mono<OrdenEvento> handle(InventarioEvento.InventarioRechazado evento) {
        var dto = InventarioEventoMapper.toDto(evento);
        return this.estadoListener.onFailure(dto)
                .then(this.ordenCumplimientoService.cancelar(evento.ordenId()))
                .map(OrdenEventoMapper::toOrdenCanceladoEvento);
    }

    @Override
    public Mono<OrdenEvento> handle(InventarioEvento.InventarioRestaurado evento) {
        var dto = InventarioEventoMapper.toDto(evento);
        return this.estadoListener.onRollback(dto)
                .then(Mono.empty());
    }
}