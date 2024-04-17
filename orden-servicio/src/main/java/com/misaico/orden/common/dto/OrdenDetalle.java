package com.misaico.orden.common.dto;

import com.misaico.orden.application.entity.OrdenCompra;
import lombok.Builder;

@Builder
public record OrdenDetalle(OrdenCompraDto orden,
                           OrdenPagoDto pago,
                           OrdenInventarioDto inventario) {
}
