package com.misaico.pago.application.repository;


import com.misaico.pago.application.entity.ClientePago;
import com.misaico.common.events.pago.PagoEstado;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PagoRepository extends ReactiveCrudRepository<ClientePago, UUID> {

    Mono<Boolean> existsByOrdenId(UUID ordenId);
    Mono<ClientePago> findByOrdenIdAndEstado(UUID ordenId, PagoEstado estado);

}
