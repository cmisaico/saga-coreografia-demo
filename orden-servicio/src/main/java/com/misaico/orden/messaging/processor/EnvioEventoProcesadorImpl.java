package com.misaico.orden.messaging.processor;

import com.misaico.common.events.envio.EnvioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.orden.common.service.envio.EnvioComponenteEstadoListener;
import com.misaico.orden.messaging.mapper.EnvioEventoMapper;
import com.misaico.common.processors.EnvioEventoProcesado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EnvioEventoProcesadorImpl implements EnvioEventoProcesado<OrdenEvento> {

    private final EnvioComponenteEstadoListener estadoListener;

    @Override
    public Mono<OrdenEvento> handle(EnvioEvento.EnvioProgramado evento) {
        var dto = EnvioEventoMapper.toDto(evento);
        return this.estadoListener.onSuccess(dto)
                .then(Mono.empty());
    }
}
