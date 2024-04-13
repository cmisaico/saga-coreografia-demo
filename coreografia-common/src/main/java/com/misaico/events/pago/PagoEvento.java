package com.misaico.events.pago;

import com.misaico.events.DomainEvent;
import com.misaico.events.OrdenSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public sealed interface PagoEvento extends DomainEvent, OrdenSaga {

    @Builder
    record PagoDescontado(UUID ordenId,
                          UUID pagoId,
                          Integer clienteId,
                          Integer monto,
                          Instant createdAt) implements PagoEvento {
    }

    @Builder
    record PagoReembolsado(UUID ordenId,
                          UUID pagoId,
                          Integer clienteId,
                          Integer monto,
                          Instant createdAt) implements PagoEvento {
    }

    @Builder
    record PagoRechazado(UUID ordenId,
                           Integer clienteId,
                           String mensaje,
                           Instant createdAt) implements PagoEvento {
    }
}
