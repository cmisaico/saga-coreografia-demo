package com.misaico.orden.common.service;

import com.misaico.orden.common.dto.OrdenCompraDto;
import com.misaico.orden.common.dto.OrdenCreaRequest;
import com.misaico.orden.common.dto.OrdenDetalle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrdenService {

    Mono<OrdenCompraDto> colocarOrden(OrdenCreaRequest request);

    Flux<OrdenCompraDto> obtenerTodasOrdenes();

    Mono<OrdenDetalle> obtenerOrdenDetalle(UUID ordenId);

}
