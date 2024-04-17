package com.misaico.orden.common.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrdenEnvioProgramado(UUID ordenId,
                                   Instant fechaEnvio) {
}
