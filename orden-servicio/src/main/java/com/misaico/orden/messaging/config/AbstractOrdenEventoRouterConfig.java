package com.misaico.orden.messaging.config;

import com.misaico.events.DomainEvent;
import com.misaico.events.orden.OrdenEvento;
import com.misaico.processors.EventoProcesador;
import com.misaico.utils.MensajeConvertidor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class AbstractOrdenEventoRouterConfig {

    private static final Logger log = LoggerFactory.getLogger(AbstractOrdenEventoRouterConfig.class);
    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    private static final String ORDEN_EVENTOS_CANAL = "orden-eventos-canal";

    protected <T extends DomainEvent> Function<Flux<Message<T>>, Flux<Message<OrdenEvento>>> procesador(EventoProcesador<T, OrdenEvento> eventoProcesador){
        return flux -> flux.map(MensajeConvertidor::toRecord)
                .doOnNext(r -> log.info("orden servicio recibido: {}", r.mensaje()))
                .concatMap(r -> eventoProcesador.procesar(r.mensaje())
                        .doOnSuccess(e -> r.acknoledgement().acknowledge())
                )
                .map(this::toMensaje);
    }

    protected Message<OrdenEvento> toMensaje(OrdenEvento evento){
        log.info("orden service producido {}", evento);
        return MessageBuilder.withPayload(evento)
                .setHeader(KafkaHeaders.KEY, evento.ordenId().toString())
                .setHeader(DESTINATION_HEADER, ORDEN_EVENTOS_CANAL)
                .build();
    }

}
