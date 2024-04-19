package com.misaico.inventario.common.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventarioDescontadoRequest(UUID ordenId,
                      Integer productoId,
                      Integer cantidad) {
}
