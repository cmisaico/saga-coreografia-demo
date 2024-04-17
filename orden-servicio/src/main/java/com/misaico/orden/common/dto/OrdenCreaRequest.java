package com.misaico.orden.common.dto;

import com.misaico.events.envio.EnvioEstado;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrdenCreaRequest(Integer clienteId,
                              Integer productoId,
                              Integer cantidad,
                              Integer precioUnidad) {
}
