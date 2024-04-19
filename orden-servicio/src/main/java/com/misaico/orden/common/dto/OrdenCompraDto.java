package com.misaico.orden.common.dto;

import com.misaico.common.events.orden.OrdenEstado;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrdenCompraDto(UUID ordenId,
                             Integer clienteId,
                             Integer productoId,
                             Integer precioUnidad,
                             Integer cantidad,
                             Integer monto,
                             OrdenEstado estado,
                             Instant fechaEnvio) {
}
