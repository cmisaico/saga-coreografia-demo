package com.misaico.orden.application.mapper;

import com.misaico.events.orden.OrdenEstado;
import com.misaico.orden.application.entity.OrdenCompra;
import com.misaico.orden.application.entity.OrdenInventario;
import com.misaico.orden.application.entity.OrdenPago;
import com.misaico.orden.common.dto.*;

public class EntityDtoMapper {
    public static OrdenCompra toOrdenCompra(OrdenCreaRequest request){
        return OrdenCompra.builder()
                .estado(OrdenEstado.PENDIENTE)
                .clienteId(request.clienteId())
                .productoId(request.productoId())
                .cantidad(request.cantidad())
                .precioUnidad(request.precioUnidad())
                .monto(request.cantidad() * request.precioUnidad())
                .build();
    }

    public static OrdenCompraDto toOrdenCompraDto(OrdenCompra ordenCompra){
        return OrdenCompraDto.builder()
                .ordenId(ordenCompra.getOrdenId())
                .precioUnidad(ordenCompra.getPrecioUnidad())
                .cantidad(ordenCompra.getCantidad())
                .productoId(ordenCompra.getProductoId())
                .monto(ordenCompra.getMonto())
                .clienteId(ordenCompra.getClienteId())
                .estado(ordenCompra.getEstado())
                .fechaEnvio(ordenCompra.getFechaEnvio())
                .build();
    }

    public static OrdenPago toOrdenPago(OrdenPagoDto dto){
        return OrdenPago.builder()
                .estado(dto.estado())
                .pagoId(dto.pagoId())
                .ordenId(dto.ordenId())
                .mensaje(dto.mensaje())
                .build();
    }

    public static OrdenPagoDto toOrdenPagoDto(OrdenPago entity) {
        return OrdenPagoDto.builder()
                .estado(entity.getEstado())
                .pagoId(entity.getPagoId())
                .ordenId(entity.getOrdenId())
                .mensaje(entity.getMensaje())
                .build();
    }

    public static OrdenInventario toOrdenInventario(OrdenInventarioDto dto) {
        return OrdenInventario.builder()
                .inventarioId(dto.inventarioId())
                .ordenId(dto.ordenId())
                .estado(dto.estado())
                .mensaje(dto.mensaje())
                .build();
    }

    public static OrdenInventarioDto toOrdenInventarioDto(OrdenInventario entity) {
        return OrdenInventarioDto.builder()
                .inventarioId(entity.getInventarioId())
                .ordenId(entity.getOrdenId())
                .estado(entity.getEstado())
                .mensaje(entity.getMensaje())
                .build();
    }

    public static OrdenDetalle toOrdenDetalle(OrdenCompraDto ordenDto,
                                              OrdenPagoDto pagoDto,
                                              OrdenInventarioDto inventarioDto){
        return OrdenDetalle.builder()
                .orden(ordenDto)
                .pago(pagoDto)
                .inventario(inventarioDto)
                .build();
    }

}


