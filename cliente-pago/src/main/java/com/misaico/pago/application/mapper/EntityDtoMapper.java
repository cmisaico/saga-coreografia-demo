package com.misaico.pago.application.mapper;

import com.misaico.pago.application.entity.ClientePago;
import com.misaico.pago.common.dto.PagoDto;
import com.misaico.pago.common.dto.PagoProcessRequest;

public class EntityDtoMapper {
    public static ClientePago toClientePago(PagoProcessRequest request){
        return ClientePago.builder()
                .clienteId(request.clienteId())
                .ordenId(request.ordenId())
                .importe(request.importe())
                .build();
    }

    public static PagoDto toDto(ClientePago pago){
        return PagoDto.builder()
                .importe(pago.getImporte())
                .estado(pago.getEstado())
                .pagoId(pago.getPagoId())
                .clienteId(pago.getClienteId())
                .ordenId(pago.getOrdenId())
                .build();
    }

}


