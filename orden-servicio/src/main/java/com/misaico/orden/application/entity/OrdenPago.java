package com.misaico.orden.application.entity;

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
public class OrdenPago {
    @Id
    private Integer id;
    private UUID ordenId;
    private UUID pagoId;
    private PagoEstado estado;
    private Boolean exito;
    private String mensaje;
}
