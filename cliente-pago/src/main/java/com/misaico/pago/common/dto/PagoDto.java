package com.misaico.pago.common.dto;

import com.misaico.common.events.pago.PagoEstado;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PagoDto(UUID pagoId,
                      UUID ordenId,
                      Integer clienteId,
                      Integer importe,
                      PagoEstado estado) {
}
