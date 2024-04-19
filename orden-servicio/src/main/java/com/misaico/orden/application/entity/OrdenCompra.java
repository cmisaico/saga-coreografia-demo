package com.misaico.orden.application.entity;

import com.misaico.common.events.orden.OrdenEstado;
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
public class OrdenCompra {

    @Id
    private UUID ordenId;
    private Integer clienteId;
    private Integer productoId;
    private Integer cantidad;
    private Integer precioUnidad;
    private Integer monto;
    private OrdenEstado estado;
    private Instant fechaEnvio;
}
