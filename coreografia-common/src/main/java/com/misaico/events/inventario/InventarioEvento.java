package com.misaico.events.inventario;

import com.misaico.events.DomainEvent;
import com.misaico.events.OrdenSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public interface InventarioEvento extends DomainEvent, OrdenSaga {

    @Builder
    record InventarioDescontado(UUID ordenId,
                                UUID inventarioId,
                                Integer cantidad,
                                Instant createdAt) implements InventarioEvento {
    }

    @Builder
    record InventarioRestaurado(UUID ordenId,
                                UUID inventarioId,
                                UUID productoid,
                                Integer cantidad,
                                Instant createdAt) implements InventarioEvento {
    }

    @Builder
    record InventarioRechazado(UUID ordenId,
                                Integer productoId,
                                Integer cantidad,
                                String mensaje,
                                Instant createdAt) implements InventarioEvento {
    }


}
