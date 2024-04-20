package com.misaico.orden;

import com.misaico.common.events.orden.OrdenEvento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@AutoConfigureWebTestClient
@TestPropertySource(properties = {
        "spring.cloud.function.definition=ordenEventoProducer;pagoProcesador;inventarioProcesador;envioProcesador;ordenEventoConsumer",
        "spring.cloud.stream.bindings.ordenEventoProducer-out-0.destination=orden-eventos",
})
public class OrdenServiceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<OrdenEvento> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<OrdenEvento> resFlux = resSink.asFlux().cache();

    @Autowired
    private WebTestClient client;

    @Test
    public void ordenCompletadoFlujoTrabajoTest(){
        var request = TestDataUtil.toRequest(1,1,2,3);
        var ordenIdRef = new AtomicReference<UUID>();
        this.client
                .post()
                .uri("/orden")
                .bodyValue(request)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$.ordenId").exists()
                .jsonPath("$.ordenId").value(id -> ordenIdRef.set(UUID.fromString(id.toString())))
                .jsonPath("$.estado").isEqualTo("PENDIENTE");
        expectEvento(OrdenEvento.OrdenCreada.class, e -> {
            Assertions.assertEquals(6, e.montoTotal());
            Assertions.assertEquals(ordenIdRef.get(), e.ordenId());
        });

    }

    private <T> void expectEvento(Class<T> type, Consumer<T> assertion){
        resFlux
                .next()
                .timeout(Duration.ofSeconds(1), Mono.empty())
                .cast(type)
                .as(StepVerifier::create)
                .consumeNextWith(assertion)
                .verifyComplete();
    }

    private void expectNoEvento(){
        resFlux
                .next()
                .timeout(Duration.ofSeconds(1), Mono.empty())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @TestConfiguration
    static class TestConfig{

        @Bean
        public Consumer<Flux<OrdenEvento>> ordenEventoConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }
    }

}
