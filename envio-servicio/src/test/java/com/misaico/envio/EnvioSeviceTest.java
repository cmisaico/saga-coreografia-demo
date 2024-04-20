package com.misaico.envio;

import com.misaico.common.events.envio.EnvioEstado;
import com.misaico.common.events.envio.EnvioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.envio.application.repository.EnvioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;


@TestPropertySource(properties = {
        "spring.cloud.function.definition=processor;ordenEventoProducer;envioEventoConsumer",
        "spring.cloud.stream.bindings.ordenEventoProducer-out-0.destination=orden-eventos",
        "spring.cloud.stream.bindings.envioEventoConsumer-in-0.destination=envio-eventos",
})
public class EnvioSeviceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<OrdenEvento> reqSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Sinks.Many<EnvioEvento> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<EnvioEvento> resFlux = resSink.asFlux().cache(0);

    @Autowired
    private EnvioRepository repository;


    @Test
    public void planearYCancelarTest(){

        // emit created event, expect no event
        var createdEvent = TestDataUtil.crearOrdenCreadoEvento(1, 1, 2, 3);
        expectNoEvent(createdEvent);

        // duplicate event, expect no event
//        expectNoEvent(createdEvent);

        // check table for just 1 record
        this.repository.findByOrdenIdAndEstado(createdEvent.ordenId(), EnvioEstado.PENDIENTE)
                .as(StepVerifier::create)
                .consumeNextWith(s -> {
                    Assertions.assertEquals(createdEvent.ordenId(), s.getOrdenId());
                    Assertions.assertEquals(createdEvent.cantidad(), s.getCantidad());
                    Assertions.assertNull(s.getFechaEnvio());
                })
                .verifyComplete();

        // emit order cancelled event
        var cancelledEvent = TestDataUtil.crearOrdenCanceladoEvento(createdEvent.ordenId());
        expectNoEvent(cancelledEvent);

        // check table for 0 record
        this.repository.findByOrdenIdAndEstado(createdEvent.ordenId(), EnvioEstado.PENDIENTE)
                .as(StepVerifier::create)
                .verifyComplete();

    }

    @Test
    public void planearYProgramarTest(){

        // emit created event, expect no event
        var createdEvent = TestDataUtil.crearOrdenCreadoEvento(1, 1, 2, 3);
        expectNoEvent(createdEvent);

        // emit order cancelled event
        var completedEvent = TestDataUtil.crearOrdenCompletadoEvento(createdEvent.ordenId());
        expectEvent(completedEvent, EnvioEvento.EnvioProgramado.class, e -> {
            Assertions.assertEquals(createdEvent.ordenId(), e.ordenId());
            Assertions.assertNotNull(e.envioId());
            Assertions.assertNotNull(e.entregaEsperada());
        });

        // duplicate completed event
        expectNoEvent(completedEvent);
    }


    private <T> void expectEvent(OrdenEvento event, Class<T> type, Consumer<T> assertion){
        resFlux
                .doFirst(() -> reqSink.tryEmitNext(event))
                .next()
                .timeout(Duration.ofSeconds(1), Mono.empty())
                .cast(type)
                .as(StepVerifier::create)
                .consumeNextWith(assertion)
                .verifyComplete();
    }

    private void expectNoEvent(OrdenEvento event){
        resFlux
                .doFirst(() -> reqSink.tryEmitNext(event))
                .next()
                .timeout(Duration.ofSeconds(1), Mono.empty())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Supplier<Flux<OrdenEvento>> ordenEventoProducer(){
            return reqSink::asFlux;
        }

        @Bean
        public Consumer<Flux<EnvioEvento>> envioEventoConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }

    }
//
//    @Test
//    public void processPagoTest(){
//        var ordenCreadoEvento = TestDataUtil.crearOrdenCreadoEvento(1,1,2,3);
//        resSink.asFlux()
//                .doFirst(() -> reqSink.tryEmitNext(ordenCreadoEvento))
//                .next()
//                .timeout(Duration.ofSeconds(1))
//                .cast(PagoEvento.PagoDescontado.class)
//                .as(StepVerifier::create)
//                .consumeNextWith(e -> {
//                    Assertions.assertNotNull(e.pagoId());
//                    Assertions.assertEquals(ordenCreadoEvento.ordenId(), e.ordenId());
//                    Assertions.assertEquals(6, e.monto());
//                })
//                .verifyComplete();
//
//    }
//
//
//    @TestConfiguration
//    static class TestConfig {
//
//        @Bean
//        public Supplier<Flux<OrdenEvento>> ordenEventoProducer(){
//            return reqSink::asFlux;
//        }
//
//        @Bean
//        public Consumer<Flux<PagoEvento>> pagoEventoConsumer(){
//            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
//        }
//
//    }

}
