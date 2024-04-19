package com.misaico.orden.messaging.config;

import com.misaico.events.envio.EnvioEvento;
import com.misaico.events.inventario.InventarioEvento;
import com.misaico.events.orden.OrdenEvento;
import com.misaico.events.pago.PagoEvento;
import com.misaico.processors.EventoProcesador;
import com.misaico.publisher.EventoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class ProcesadorConfig extends AbstractOrdenEventoRouterConfig {

    private final EventoProcesador<InventarioEvento, OrdenEvento> inventarioEventoProcesador;
    private final EventoProcesador<PagoEvento, OrdenEvento> pagoEventoProcesador;
    private final EventoProcesador<EnvioEvento, OrdenEvento> envioEventoProcesador;
    private final EventoPublisher<OrdenEvento> eventoPublisher;

    @Bean
    public Function<Flux<Message<InventarioEvento>>, Flux<Message<OrdenEvento>>> inventarioProcesador(){
        return this.procesador(inventarioEventoProcesador);
    }

    @Bean
    public Function<Flux<Message<PagoEvento>>, Flux<Message<OrdenEvento>>> pagoProcesador(){
        return this.procesador(pagoEventoProcesador);
    }

    @Bean
    public Function<Flux<Message<EnvioEvento>>, Flux<Message<OrdenEvento>>> envioProcesador(){
        return this.procesador(envioEventoProcesador);
    }

    @Bean
    public Supplier<Flux<Message<OrdenEvento>>> ordenEventoProducer(){
        return () -> this.eventoPublisher.publish()
                .map(this::toMensaje);
    }

}
