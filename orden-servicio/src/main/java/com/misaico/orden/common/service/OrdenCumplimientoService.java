package com.misaico.orden.common.service;

import com.misaico.orden.common.dto.OrdenCompraDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrdenCumplimientoService {
    Mono<OrdenCompraDto> completar(UUID ordenId);
    Mono<OrdenCompraDto> cancelar(UUID ordenId);
}
