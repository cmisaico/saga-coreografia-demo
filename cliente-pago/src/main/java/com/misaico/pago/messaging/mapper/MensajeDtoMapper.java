package com.misaico.pago.messaging.mapper;

import com.misaico.pago.common.dto.PagoDto;
import com.misaico.pago.common.dto.PagoProcessRequest;
import com.misaico.events.orden.OrdenEvento;
import com.misaico.events.pago.PagoEvento;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

public class MensajeDtoMapper {

    public static PagoProcessRequest toPagoProcessRequest(OrdenEvento.OrdenCreada evento){
        return PagoProcessRequest.builder()
                .clienteid(evento.clienteId())
                .ordenId(evento.ordenId())
                .importe(evento.montoTotal())
                .build();
    }

    public static PagoEvento toPagoDescontadoEvento(PagoDto dto){
        return PagoEvento.PagoDescontado.builder()
                .pagoId(dto.pagoId())
                .ordenId(dto.ordenId())
                .monto(dto.importe())
                .clienteId(dto.clienteId())
                .createdAt(Instant.now())
                .build();
    }

    public static PagoEvento toPagoReembolsadoEvento(PagoDto dto){
        return PagoEvento.PagoReembolsado.builder()
                .pagoId(dto.pagoId())
                .ordenId(dto.ordenId())
                .monto(dto.importe())
                .clienteId(dto.clienteId())
                .createdAt(Instant.now())
                .build();
    }

    public static Function<Throwable, Mono<PagoEvento>> toPagoRechazadoEvento(OrdenEvento.OrdenCreada evento){
        return ex -> Mono.fromSupplier(() -> PagoEvento.PagoRechazado.builder()
                .ordenId(evento.ordenId())
                .monto(evento.montoTotal())
                .clienteId(evento.clienteId())
                .createdAt(Instant.now())
                .mensaje(ex.getMessage())
                .build());
    }


}
