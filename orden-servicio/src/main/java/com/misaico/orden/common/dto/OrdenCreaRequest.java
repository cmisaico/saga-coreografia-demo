package com.misaico.orden.common.dto;

import lombok.Builder;

@Builder
public record OrdenCreaRequest(Integer clienteId,
                              Integer productoId,
                              Integer cantidad,
                              Integer precioUnidad) {
}
