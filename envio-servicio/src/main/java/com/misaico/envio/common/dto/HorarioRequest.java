package com.misaico.envio.common.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HorarioRequest(UUID ordenId,
                      Integer productoId,
                      Integer clienteId,
                      Integer cantidad) {
}
