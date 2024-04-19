package com.misaico.orden.application.repository;

import com.misaico.events.orden.OrdenEstado;
import com.misaico.orden.application.entity.OrdenCompra;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrdenCompraRepository extends ReactiveCrudRepository<OrdenCompra, UUID> {

    Mono<OrdenCompra> findByOrdenIdAndEstado(UUID ordenId, OrdenEstado estado);

    @Query("""
            SELECT oc.*
            FROM orden_compra oc,
                 orden_pago op,
                 orden_inventario oi
            WHERE op.orden_id = pc.orden_id
                  AND oi.orden_id = po.orden_id
                  AND op.exito
                  AND oi.exito
                  AND oc.estado = 'PENDIENTE'
                  AND oc.orden_id = :ordenId
            """)
    Mono<OrdenCompra> getWhenOrdenComponenteCompletado(UUID ordenId);


}
