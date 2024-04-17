package com.misaico.orden.common.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrdenComponenteFetcher<T> {

    Mono<T> obtenerComponente(UUID ordenId);

}
