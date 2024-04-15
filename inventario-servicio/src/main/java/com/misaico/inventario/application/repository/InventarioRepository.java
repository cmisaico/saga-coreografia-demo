package com.misaico.inventario.application.repository;


import com.misaico.events.inventario.InventarioEstado;
import com.misaico.inventario.application.entity.InventarioOrden;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface InventarioRepository extends ReactiveCrudRepository<InventarioOrden, UUID> {

    Mono<Boolean> existsByOrdenId(UUID ordenId);
    Mono<InventarioOrden> findByOrdenIdAndEstado(UUID ordenId, InventarioEstado estado);

}
