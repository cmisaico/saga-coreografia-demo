package com.misaico.orden.common.dto;

import com.misaico.common.events.inventario.InventarioEstado;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrdenInventarioDto(UUID ordenId,
                      UUID inventarioId,
                      InventarioEstado estado,
                      String mensaje) {
}
