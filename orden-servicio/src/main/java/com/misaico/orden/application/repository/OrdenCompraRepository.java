package com.misaico.orden.application.repository;

import com.misaico.events.orden.OrdenEstado;
import com.misaico.orden.application.entity.OrdenCompra;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrdenCompraRepository extends ReactiveCrudRepository<OrdenCompra, UUID> {

    Mono<OrdenCompra> findByOrdenIdAndEstado(UUID ordenId, OrdenEstado estado);

}
