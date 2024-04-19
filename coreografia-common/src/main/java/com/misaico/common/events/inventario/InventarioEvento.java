package com.misaico.common.events.inventario;

import com.misaico.common.events.DomainEvent;
import com.misaico.common.events.OrdenSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public interface InventarioEvento extends DomainEvent, OrdenSaga {

    @Builder
    record InventarioDescontado(UUID ordenId,
                                UUID inventarioId,
                                Integer productoId,
                                Integer cantidad,
                                Instant createdAt) implements InventarioEvento {
    }

    @Builder
    record InventarioRestaurado(UUID ordenId,
                                UUID inventarioId,
                                Integer productoId,
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
