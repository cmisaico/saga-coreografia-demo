package com.misaico.envio.application.repository;


import com.misaico.envio.application.entity.Envio;
import com.misaico.events.envio.EnvioEstado;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EnvioRepository extends ReactiveCrudRepository<Envio, UUID> {

    Mono<Boolean> existsByOrdenId(UUID ordenId);
    Mono<Envio> findByOrdenIdAndEstado(UUID ordenId, EnvioEstado estado);
    Mono<Void> deleteByOrdenId(UUID ordenId);

}
