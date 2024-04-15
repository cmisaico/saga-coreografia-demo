package com.misaico.inventario.application.mapper;

import com.misaico.inventario.application.entity.InventarioOrden;
import com.misaico.inventario.common.dto.InventarioDescontadoRequest;
import com.misaico.inventario.common.dto.InventarioOrdenDto;

public class EntityDtoMapper {
    public static InventarioOrden toInventarioOrden(InventarioDescontadoRequest request){
        return InventarioOrden.builder()
                .ordenId(request.ordenId())
                .productoId(request.productoId())
                .cantidad(request.cantidad())
                .build();
    }

    public static InventarioOrdenDto toDto(InventarioOrden inventarioOrden){
        return InventarioOrdenDto.builder()
                .inventarioId(inventarioOrden.getInventarioId())
                .ordenId(inventarioOrden.getOrdenId())
                .productoId(inventarioOrden.getProductoId())
                .cantidad(inventarioOrden.getCantidad())
                .estado(inventarioOrden.getEstado())
                .build();
    }

}


