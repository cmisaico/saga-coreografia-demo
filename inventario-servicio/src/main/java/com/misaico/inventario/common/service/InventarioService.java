package com.misaico.inventario.common.service;

import com.misaico.inventario.common.dto.InventarioDescontadoRequest;
import com.misaico.inventario.common.dto.InventarioOrdenDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InventarioService {

    Mono<InventarioOrdenDto> descontar(InventarioDescontadoRequest request);

    Mono<InventarioOrdenDto> restaurar(UUID ordenId);

}
