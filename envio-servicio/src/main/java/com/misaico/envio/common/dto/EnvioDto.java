package com.misaico.envio.common.dto;

import com.misaico.common.events.envio.EnvioEstado;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record EnvioDto(UUID envioId,
                              UUID ordenId,
                              Integer productoId,
                              Integer clienteId,
                              Integer cantidad,
                              Instant fechaEnvio,
                              EnvioEstado estado) {
}
