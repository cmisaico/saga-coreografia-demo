package com.misaico.inventario.common.dto;

import com.misaico.events.pago.PagoEstado;
import lombok.Builder;

import java.util.UUID;

@Builder
public record InventarioDescontadoRequest(UUID ordenId,
                      Integer productoId,
                      Integer cantidad) {
}
