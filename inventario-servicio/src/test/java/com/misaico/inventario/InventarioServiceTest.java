package com.misaico.inventario;

import com.misaico.common.events.inventario.InventarioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.events.pago.PagoEvento;
import com.misaico.inventario.application.repository.ProductoRepository;
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
        "spring.cloud.function.definition=processor;ordenEventoProducer;inventarioEventoConsumer",
        "spring.cloud.stream.bindings.ordenEventoProducer-out-0.destination=orden-eventos",
        "spring.cloud.stream.bindings.inventarioEventoConsumer-in-0.destination=inventario-eventos",
})
public class InventarioServiceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<OrdenEvento> reqSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Sinks.Many<InventarioEvento> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<InventarioEvento> resFlux = resSink.asFlux().cache(0);

    @Autowired
    private ProductoRepository repository;

    @Test
    public void deductAndRefundTest(){

        // deduct payment
        var orderCreatedEvent = TestDataUtil.crearOrdenCreadoEvento(1, 1, 2, 3);
        expectEvent(orderCreatedEvent, InventarioEvento.InventarioDescontado.class, e -> {
            Assertions.assertNotNull(e.inventarioId());
            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
            Assertions.assertEquals(3, e.cantidad());
        });

        // check balance
        this.repository.findById(1)
                .as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(7, c.getCantidadDisponible()))
                .verifyComplete();

        // duplicate event
        expectNoEvent(orderCreatedEvent);

        // cancelled event & refund
        var cancelledEvent = TestDataUtil.crearOrdenCanceladoEvento(orderCreatedEvent.ordenId());
        expectEvent(cancelledEvent, InventarioEvento.InventarioRestaurado.class, e -> {
            Assertions.assertNotNull(e.inventarioId());
            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
            Assertions.assertEquals(3, e.cantidad());
        });

        // check balance
        this.repository.findById(1)
                .as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(10, c.getCantidadDisponible()))
                .verifyComplete();

    }

    @Test
    public void refundWithoutDeductTest(){
        var cancelledEvent = TestDataUtil.crearOrdenCanceladoEvento(UUID.randomUUID());
        expectNoEvent(cancelledEvent);
    }

    @Test
    public void outOfStockErrorTest(){
        var orderCreatedEvent = TestDataUtil.crearOrdenCreadoEvento(1, 1, 2, 11);
        expectEvent(orderCreatedEvent, InventarioEvento.InventarioRechazado.class, e -> {
            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
            Assertions.assertEquals(11, e.cantidad());
            Assertions.assertEquals("No hay stock", e.mensaje());
        });
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
        public Consumer<Flux<InventarioEvento>> inventarioEventoConsumer(){
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
