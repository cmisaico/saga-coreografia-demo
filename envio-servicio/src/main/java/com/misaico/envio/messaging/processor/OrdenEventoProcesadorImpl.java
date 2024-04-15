package com.misaico.envio.messaging.processor;

import com.misaico.envio.common.service.EnvioService;
import com.misaico.envio.messaging.mapper.MensajeDtoMapper;
import com.misaico.events.envio.EnvioEvento;
import com.misaico.events.orden.OrdenEvento;
import com.misaico.exceptions.EventoYaProcesadoException;
import com.misaico.processors.OrdenEventoProcesador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class OrdenEventoProcesadorImpl implements OrdenEventoProcesador<EnvioEvento> {

    private static final Logger log = LoggerFactory.getLogger(OrdenEventoProcesadorImpl.class);
    private final EnvioService service;
    @Override
    public Mono<EnvioEvento> handle(OrdenEvento.OrdenCreada evento) {
        return this.service.agregarEnvio(MensajeDtoMapper.toHorarioRequest(evento))
                .transform(exceptionHandler())
                .then(Mono.empty());
    }

    @Override
    public Mono<EnvioEvento> handle(OrdenEvento.OrdenCancelada evento) {
        return this.service.cancelar(evento.ordenId())
                .then(Mono.empty());
    }

    @Override
    public Mono<EnvioEvento> handle(OrdenEvento.OrdenCompletada evento) {
        return this.service.programar(evento.ordenId())
                .map(MensajeDtoMapper::toEnvioProgramadoEvento)
                .doOnNext(e -> log.info("envio programado {}", e));
    }

    private <T> UnaryOperator<Mono<T>> exceptionHandler(){
        return mono -> mono.onErrorResume(EventoYaProcesadoException.class, e -> Mono.empty())
                .doOnError(ex -> log.error(ex.getMessage()));
    }
}
