package com.misaico.events.orden;

import com.misaico.events.DomainEvent;
import com.misaico.events.OrdenSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public interface OrdenEvento extends DomainEvent, OrdenSaga {

    @Builder
    record OrdenCreada(UUID ordenId,
                        Integer productoId,
                        Integer clienteId,
                        Integer cantidad,
                        Integer precioUnidad,
                        Integer montoTotal,
                        Instant createdAt) implements OrdenEvento {

    }

    @Builder
    record OrdenCancelada(UUID ordenId,
                        String mensaje,
                        Instant createdAt) implements OrdenEvento {

    }

    @Builder
    record OrdenCompletada(UUID ordenId,
                          Instant createdAt) implements OrdenEvento {

    }
}
