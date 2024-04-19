package com.misaico.common.events.pago;

import com.misaico.common.events.DomainEvent;
import com.misaico.common.events.OrdenSaga;
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
                           Integer monto,
                           String mensaje,
                           Instant createdAt) implements PagoEvento {
    }
}
