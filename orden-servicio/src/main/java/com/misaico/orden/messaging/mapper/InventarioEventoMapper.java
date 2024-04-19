package com.misaico.orden.messaging.mapper;

import com.misaico.common.events.inventario.InventarioEstado;
import com.misaico.common.events.inventario.InventarioEvento;
import com.misaico.orden.common.dto.OrdenInventarioDto;

public class InventarioEventoMapper {

    public static OrdenInventarioDto toDto(InventarioEvento.InventarioDescontado evento){
        return OrdenInventarioDto.builder()
                .ordenId(evento.ordenId())
                .inventarioId(evento.inventarioId())
                .estado(InventarioEstado.DESCONTADO)
                .build();
    }

    public static OrdenInventarioDto toDto(InventarioEvento.InventarioRechazado evento){
        return OrdenInventarioDto.builder()
                .ordenId(evento.ordenId())
                .estado(InventarioEstado.RECHAZADO)
                .mensaje(evento.mensaje())
                .build();
    }

    public static  OrdenInventarioDto toDto(InventarioEvento.InventarioRestaurado evento){
        return OrdenInventarioDto.builder()
                .ordenId(evento.ordenId())
                .estado(InventarioEstado.RESTAURADO)
                .build();
    }

}
