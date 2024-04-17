package com.misaico.orden.application.repository;

import com.misaico.orden.application.entity.OrdenInventario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrdenInventarioRepository extends ReactiveCrudRepository<OrdenInventario, UUID> {

    Mono<OrdenInventario> findByOrdenId(UUID ordenId);
}
