package com.misaico.orden.messaging.config;

import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.orden.common.service.OrdenEventoListener;
import com.misaico.orden.messaging.publisher.OrdenEventoListenerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class OrdenEventoListenerConfig {

    @Bean
    public OrdenEventoListener ordenEventoListener(){
        var sink = Sinks.many().unicast().<OrdenEvento> onBackpressureBuffer();
        var flux = sink.asFlux();
        return new OrdenEventoListenerImpl(sink, flux);
    }

}
