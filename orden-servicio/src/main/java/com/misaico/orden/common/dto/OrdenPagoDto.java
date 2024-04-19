package com.misaico.orden.common.dto;

import com.misaico.common.events.pago.PagoEstado;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrdenPagoDto(UUID ordenId,
                           UUID pagoId,
                           PagoEstado estado,
                           String mensaje
                           ) {
}
