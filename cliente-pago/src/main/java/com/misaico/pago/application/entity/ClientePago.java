package com.misaico.pago.application.entity;

import com.misaico.common.events.pago.PagoEstado;
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
public class ClientePago {
    @Id
    private UUID pagoId;
    private UUID ordenId;
    private Integer clienteId;
    private PagoEstado estado;
    private Integer importe;

}
