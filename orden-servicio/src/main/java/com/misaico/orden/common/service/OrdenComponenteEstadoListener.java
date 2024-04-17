package com.misaico.orden.common.service;

import reactor.core.publisher.Mono;

public interface OrdenComponenteEstadoListener<T> {
    Mono<Void> onSuccess(T t);
    Mono<Void> onFailure(T t);
    Mono<Void> onRollback(T t);

}
