package com.misaico.utils;

import com.misaico.exceptions.EventoYaProcesadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class DuplicadoEventoValidador {

    private static final Logger log = LoggerFactory.getLogger(DuplicadoEventoValidador.class);

    public static Function<Mono<Boolean>, Mono<Void>> emitirErrorPorProcesoRedundante(){
        return mono -> mono
                .flatMap(b -> b ? Mono.error(new EventoYaProcesadoException()) : Mono.empty())
                .doOnError(EventoYaProcesadoException.class, ex -> log.warn("Evento duplicado"))
                .then();
    }

    public static <T> Mono<T> validar(Mono<Boolean> eventoValidacionPublisher, Mono<T> eventoProcesandoPublisher){
        return eventoValidacionPublisher
                .transform(emitirErrorPorProcesoRedundante())
                .then(eventoProcesandoPublisher);
    }

}
