package com.misaico.pago.common.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PagoProcessRequest(Integer clienteId,
                                 UUID ordenId,
                                 Integer importe) {
}
