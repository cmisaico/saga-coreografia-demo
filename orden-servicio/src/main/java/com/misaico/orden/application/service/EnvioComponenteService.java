package com.misaico.orden.application.service;

import com.misaico.events.orden.OrdenEstado;
import com.misaico.orden.application.repository.OrdenCompraRepository;
import com.misaico.orden.common.dto.OrdenEnvioProgramado;
import com.misaico.orden.common.dto.OrdenInventarioDto;
import com.misaico.orden.common.service.envio.EnvioComponenteEstadoListener;
import com.misaico.orden.common.service.envio.EnvioComponenteFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvioComponenteService implements EnvioComponenteEstadoListener {

    private final OrdenCompraRepository repository;
    @Override
    public Mono<Void> onSuccess(OrdenEnvioProgramado mensaje) {
        return this.repository.findByOrdenIdAndEstado(mensaje.ordenId(), OrdenEstado.COMPLETADO)
                .doOnNext(e -> e.setFechaEnvio(mensaje.fechaEnvio()))
                .flatMap(this.repository::save)
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrdenEnvioProgramado mensaje) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> onRollback(OrdenEnvioProgramado mensaje) {
        return Mono.empty();
    }

}
