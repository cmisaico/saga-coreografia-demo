package com.misaico.application.entity;

import com.misaico.events.pago.PagoEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ClientePago {
    @Id
    private UUID pagoId;
    private UUID ordenId;
    private Integer clienteId;
    private PagoEstado estado;
    private Integer importe;

}
