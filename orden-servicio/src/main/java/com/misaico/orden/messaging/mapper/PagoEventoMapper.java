package com.misaico.orden.messaging.mapper;

import com.misaico.common.events.pago.PagoEstado;
import com.misaico.common.events.pago.PagoEvento;
import com.misaico.orden.common.dto.OrdenPagoDto;

public class PagoEventoMapper {

    public static OrdenPagoDto toDto(PagoEvento.PagoDescontado evento){
        return OrdenPagoDto.builder()
                .ordenId(evento.ordenId())
                .pagoId(evento.pagoId())
                .estado(PagoEstado.DESCONTADO)
                .build();
    }

    public static OrdenPagoDto toDto(PagoEvento.PagoRechazado evento){
        return OrdenPagoDto.builder()
                .ordenId(evento.ordenId())
                .estado(PagoEstado.RECHAZADO)
                .mensaje(evento.mensaje())
                .build();
    }

    public static  OrdenPagoDto toDto(PagoEvento.PagoReembolsado evento){
        return OrdenPagoDto.builder()
                .ordenId(evento.ordenId())
                .estado(PagoEstado.REEMBOLSADO)
                .build();
    }

}
