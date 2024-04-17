package com.misaico.orden.application.repository;

import com.misaico.orden.application.entity.OrdenPago;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrdenPagoRepository extends ReactiveCrudRepository<OrdenPago, Integer> {

    Mono<OrdenPago> findByOrdenId(UUID ordenId);

}
