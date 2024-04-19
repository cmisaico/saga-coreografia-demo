package com.misaico.orden.messaging.mapper;

import com.misaico.events.envio.EnvioEvento;
import com.misaico.events.orden.OrdenEvento;
import com.misaico.orden.common.dto.OrdenCompraDto;

import java.time.Instant;

public class OrdenEventoMapper {

    public static OrdenEvento toOrdenCreatedEvento(OrdenCompraDto dto){
        return OrdenEvento.OrdenCreada.builder()
                .ordenId(dto.ordenId())
                .precioUnidad(dto.precioUnidad())
                .cantidad(dto.cantidad())
                .productoId(dto.productoId())
                .montoTotal(dto.monto())
                .clienteId(dto.clienteId())
                .createdAt(Instant.now())
                .build();
    }

    public static OrdenEvento toOrdenCanceladoEvento(OrdenCompraDto dto){
        return OrdenEvento.OrdenCancelada.builder()
                .ordenId(dto.ordenId())
                .createdAt(Instant.now())
                .build();
    }

    public static OrdenEvento toOrdenCompletadoEvento(OrdenCompraDto dto){
        return OrdenEvento.OrdenCompletada.builder()
                .ordenId(dto.ordenId())
                .createdAt(Instant.now())
                .build();
    }

}
