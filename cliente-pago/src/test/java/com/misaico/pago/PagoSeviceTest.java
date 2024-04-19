package com.misaico.pago;

import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.events.pago.PagoEvento;
import com.misaico.pago.application.repository.ClienteRepository;
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
        "spring.cloud.function.definition=processor;ordenEventoProducer;pagoEventoConsumer",
        "spring.cloud.stream.bindings.ordenEventoProducer-out-0.destination=orden-eventos",
        "spring.cloud.stream.bindings.pagoEventoConsumer-in-0.destination=pago-eventos",
})
public class PagoSeviceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<OrdenEvento> reqSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Sinks.Many<PagoEvento> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<PagoEvento> resFlux = resSink.asFlux().cache(0);

    @Autowired
    private ClienteRepository repository;

//    @Test
//    public void deductAndRefundTest(){
//
//        // deduct payment
//        var orderCreatedEvent = TestDataUtil.crearOrdenCreadoEvento(1, 1, 2, 3);
//        expectEvent(orderCreatedEvent, PagoEvento.PagoDescontado.class, e -> {
//            Assertions.assertNotNull(e.pagoId());
//            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
//            Assertions.assertEquals(6, e.monto());
//        });
//
//        // check balance
//        this.repository.findById(1)
//                .as(StepVerifier::create)
//                .consumeNextWith(c -> Assertions.assertEquals(94, c.getSaldo()))
//                .verifyComplete();
//
//        // duplicate event
//        expectNoEvent(orderCreatedEvent);
//
//        // cancelled event & refund
//        var cancelledEvent = TestDataUtil.crearOrdenCanceladoEvento(orderCreatedEvent.ordenId());
//        expectEvent(cancelledEvent, PagoEvento.PagoReembolsado.class, e -> {
//            Assertions.assertNotNull(e.pagoId());
//            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
//            Assertions.assertEquals(6, e.monto());
//        });
//
//        // check balance
//        this.repository.findById(1)
//                .as(StepVerifier::create)
//                .consumeNextWith(c -> Assertions.assertEquals(100, c.getSaldo()))
//                .verifyComplete();
//
//    }
//
//    @Test
//    public void refundWithoutDeductTest(){
//        var cancelledEvent = TestDataUtil.crearOrdenCanceladoEvento(UUID.randomUUID());
//        expectNoEvent(cancelledEvent);
//    }
//
//    @Test
//    public void customerNotFoundTest(){
//        var orderCreatedEvent = TestDataUtil.crearOrdenCreadoEvento(10, 1, 2, 3);
//        expectEvent(orderCreatedEvent, PagoEvento.PagoRechazado.class, e -> {
//            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
//            Assertions.assertEquals(6, e.monto());
//            Assertions.assertEquals("Customer not found", e.mensaje());
//        });
//    }
//
//    @Test
//    public void insufficientBalanceTest(){
//        var orderCreatedEvent = TestDataUtil.crearOrdenCreadoEvento(1, 1, 2, 51);
//        expectEvent(orderCreatedEvent, PagoEvento.PagoRechazado.class, e -> {
//            Assertions.assertEquals(orderCreatedEvent.ordenId(), e.ordenId());
//            Assertions.assertEquals(102, e.monto());
//            Assertions.assertEquals("Customer does not have enough balance", e.mensaje());
//        });
//    }
//
//    private <T> void expectEvent(OrdenEvento event, Class<T> type, Consumer<T> assertion){
//        resFlux
//                .doFirst(() -> reqSink.tryEmitNext(event))
//                .next()
//                .timeout(Duration.ofSeconds(1), Mono.empty())
//                .cast(type)
//                .as(StepVerifier::create)
//                .consumeNextWith(assertion)
//                .verifyComplete();
//    }
//
//    private void expectNoEvent(OrdenEvento event){
//        resFlux
//                .doFirst(() -> reqSink.tryEmitNext(event))
//                .next()
//                .timeout(Duration.ofSeconds(1), Mono.empty())
//                .as(StepVerifier::create)
//                .verifyComplete();
//    }
//
//    @TestConfiguration
//    static class TestConfig {
//
//        @Bean
//        public Supplier<Flux<OrdenEvento>> orderEventProducer(){
//            return reqSink::asFlux;
//        }
//
//        @Bean
//        public Consumer<Flux<PagoEvento>> paymentEventConsumer(){
//            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
//        }
//
//    }

    @Test
    public void processPagoTest(){
        var ordenCreadoEvento = TestDataUtil.crearOrdenCreadoEvento(1,1,2,3);
        resSink.asFlux()
                .doFirst(() -> reqSink.tryEmitNext(ordenCreadoEvento))
                .next()
                .timeout(Duration.ofSeconds(1))
                .cast(PagoEvento.PagoDescontado.class)
                .as(StepVerifier::create)
                .consumeNextWith(e -> {
                    Assertions.assertNotNull(e.pagoId());
                    Assertions.assertEquals(ordenCreadoEvento.ordenId(), e.ordenId());
                    Assertions.assertEquals(6, e.monto());
                })
                .verifyComplete();

    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        public Supplier<Flux<OrdenEvento>> ordenEventoProducer(){
            return reqSink::asFlux;
        }

        @Bean
        public Consumer<Flux<PagoEvento>> pagoEventoConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }

    }

}
