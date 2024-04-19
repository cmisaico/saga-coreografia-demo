package com.misaico.inventario.application.entity;

import com.misaico.common.events.inventario.InventarioEstado;
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
