package com.misaico.inventario.messaging.mapper;

import com.misaico.common.events.inventario.InventarioEvento;
import com.misaico.common.events.orden.OrdenEvento;
import com.misaico.common.events.pago.PagoEvento;
import com.misaico.inventario.common.dto.InventarioDescontadoRequest;
import com.misaico.inventario.common.dto.InventarioOrdenDto;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

public class MensajeDtoMapper {

    public static InventarioDescontadoRequest toInventarioDescontadoRequest(OrdenEvento.OrdenCreada evento){
        return InventarioDescontadoRequest.builder()
                .ordenId(evento.ordenId())
                .productoId(evento.productoId())
                .cantidad(evento.cantidad())
                .build();
    }

    public static InventarioEvento toInventarioDescontadoEvento(InventarioOrdenDto inventarioOrdenDto){
        return InventarioEvento.InventarioDescontado.builder()
                .ordenId(inventarioOrdenDto.ordenId())
                .inventarioId(inventarioOrdenDto.inventarioId())
                .productoId(inventarioOrdenDto.productoId())
                .cantidad(inventarioOrdenDto.cantidad())
                .createdAt(Instant.now())
                .build();
    }

    public static InventarioEvento toInventarioRestauradoEvento(InventarioOrdenDto inventarioOrdenDto){
        return InventarioEvento.InventarioRestaurado.builder()
                .ordenId(inventarioOrdenDto.ordenId())
                .inventarioId(inventarioOrdenDto.inventarioId())
                .productoId(inventarioOrdenDto.productoId())
                .cantidad(inventarioOrdenDto.cantidad())
                .createdAt(Instant.now())
                .build();
    }

    public static Function<Throwable, Mono<InventarioEvento>> toInventarioRechazadoEvento(OrdenEvento.OrdenCreada evento){
        return ex -> Mono.fromSupplier(() -> InventarioEvento.InventarioRechazado.builder()
                .ordenId(evento.ordenId())
                .productoId(evento.productoId())
                .cantidad(evento.cantidad())
                .createdAt(Instant.now())
                .mensaje(ex.getMessage())
                .build()
        );
    }


}
