package com.misaico.inventario.common.dto;

import com.misaico.common.events.inventario.InventarioEstado;
import lombok.Builder;

import java.util.UUID;

@Builder
public record InventarioOrdenDto(UUID inventarioId,
                              UUID ordenId,
                              Integer productoId,
                              Integer cantidad,
                              InventarioEstado estado) {
}
