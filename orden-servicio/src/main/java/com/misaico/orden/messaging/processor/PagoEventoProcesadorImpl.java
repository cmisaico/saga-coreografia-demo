package com.misaico.orden.messaging.processor;

import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.events.pago.PagoEvento;
import com.misaico.orden.common.service.OrdenCumplimientoService;
import com.misaico.orden.common.service.pago.PagoComponenteEstadoListener;
import com.misaico.orden.messaging.mapper.OrdenEventoMapper;
import com.misaico.orden.messaging.mapper.PagoEventoMapper;
import com.misaico.common.processors.PagoEventoProcesador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PagoEventoProcesadorImpl implements PagoEventoProcesador<OrdenEvento> {

    private final OrdenCumplimientoService cumplimientoService;
    private final PagoComponenteEstadoListener estadoListener;

    @Override
    public Mono<OrdenEvento> handle(PagoEvento.PagoDescontado evento) {
        var dto = PagoEventoMapper.toDto(evento);
        return this.estadoListener.onSuccess(dto)
                .then(this.cumplimientoService.completar(evento.ordenId()))
                .map(OrdenEventoMapper::toOrdenCompletadoEvento);
    }

    @Override
    public Mono<OrdenEvento> handle(PagoEvento.PagoRechazado evento) {
        var dto = PagoEventoMapper.toDto(evento);
        return this.estadoListener.onFailure(dto)
                .then(this.cumplimientoService.cancelar(evento.ordenId()))
                .map(OrdenEventoMapper::toOrdenCanceladoEvento);
    }

    @Override
    public Mono<OrdenEvento> handle(PagoEvento.PagoReembolsado evento) {
        var dto = PagoEventoMapper.toDto(evento);
        return this.estadoListener.onRollback(dto)
                .then(Mono.empty());
    }
}
