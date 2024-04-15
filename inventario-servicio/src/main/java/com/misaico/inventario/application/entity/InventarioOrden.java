package com.misaico.inventario.application.entity;

import com.misaico.events.inventario.InventarioEstado;
import com.misaico.events.pago.PagoEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioOrden {
    @Id
    private UUID inventarioId;
    private UUID ordenId;
    private Integer productoId;
    private Integer cantidad;
    private InventarioEstado estado;

}
