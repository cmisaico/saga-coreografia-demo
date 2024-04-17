package com.misaico.orden.application.entity;

import com.misaico.events.inventario.InventarioEstado;
import com.misaico.events.orden.OrdenEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenInventario {
    @Id
    private Integer id;
    private UUID ordenId;
    private UUID inventarioId;
    private InventarioEstado estado;
    private String mensaje;
    private Boolean exito;
}
