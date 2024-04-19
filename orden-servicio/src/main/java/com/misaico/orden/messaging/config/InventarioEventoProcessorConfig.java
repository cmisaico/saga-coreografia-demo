package com.misaico.orden.messaging.config;


import com.misaico.common.events.inventario.InventarioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.processors.InventarioEventoProcesador;
import com.misaico.common.utils.MensajeConvertidor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

//@Configuration
@RequiredArgsConstructor
public class InventarioEventoProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(InventarioEventoProcessorConfig.class);
    private final InventarioEventoProcesador<OrdenEvento> eventoProcesador;

//    @Bean
    public Function<Flux<Message<InventarioEvento>>, Flux<Message<OrdenEvento>>> processor(){
        return flux -> flux.map(MensajeConvertidor::toRecord)
                .doOnNext(r -> log.info("orden servicio recibido: {}", r.mensaje()))
                .concatMap(r -> this.eventoProcesador.procesar(r.mensaje())
                        .doOnSuccess(e -> r.acknoledgement().acknowledge())
                )
                .map(this::toMensaje);
    }

    private Message<OrdenEvento> toMensaje(OrdenEvento evento){
        return MessageBuilder.withPayload(evento)
                .setHeader(KafkaHeaders.KEY, evento.ordenId().toString())
                .build();
    }

}
