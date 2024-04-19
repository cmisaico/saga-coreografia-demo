package com.misaico.envio.messaging.config;


import com.misaico.common.events.envio.EnvioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.processors.OrdenEventoProcesador;
import com.misaico.common.utils.MensajeConvertidor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class OrdenEventProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(OrdenEventProcessorConfig.class);
    private final OrdenEventoProcesador<EnvioEvento> eventoProcesador;

    @Bean
    public Function<Flux<Message<OrdenEvento>>, Flux<Message<EnvioEvento>>> processor(){
        return flux -> flux.map(MensajeConvertidor::toRecord)
                .doOnNext(r -> log.info("envio servicio recibido: {}", r.mensaje()))
                .concatMap(r -> this.eventoProcesador.procesar(r.mensaje())
                        .doOnSuccess(e -> r.acknoledgement().acknowledge())
                )
                .map(this::toMensaje);
    }

    private Message<EnvioEvento> toMensaje(EnvioEvento evento){
        return MessageBuilder.withPayload(evento)
                .setHeader(KafkaHeaders.KEY, evento.ordenId().toString())
                .build();
    }

}
