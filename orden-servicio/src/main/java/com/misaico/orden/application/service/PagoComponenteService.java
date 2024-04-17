package com.misaico.orden.application.service;

import com.misaico.orden.application.entity.OrdenPago;
import com.misaico.orden.application.mapper.EntityDtoMapper;
import com.misaico.orden.application.repository.OrdenPagoRepository;
import com.misaico.orden.common.dto.OrdenPagoDto;
import com.misaico.orden.common.service.pago.PagoComponenteEstadoListener;
import com.misaico.orden.common.service.pago.PagoComponenteFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoComponenteService implements PagoComponenteFetcher, PagoComponenteEstadoListener {

    private final OrdenPagoRepository repository;
    private static final OrdenPagoDto DEFAULT = OrdenPagoDto.builder().build();

    @Override
    public Mono<OrdenPagoDto> obtenerComponente(UUID ordenId) {
        return this.repository.findByOrdenId(ordenId)
                .map(EntityDtoMapper::toOrdenPagoDto)
                .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrdenPagoDto mensaje) {
        return this.repository.findByOrdenId(mensaje.ordenId())
                .switchIfEmpty(Mono.defer(() -> this.add(mensaje, true)))
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrdenPagoDto mensaje) {
        return this.repository.findByOrdenId(mensaje.ordenId())
                .switchIfEmpty(Mono.defer(() -> this.add(mensaje, false)))
                .then();
    }

    @Override
    public Mono<Void> onRollback(OrdenPagoDto mensaje) {
        return this.repository.findByOrdenId(mensaje.ordenId())
                .doOnNext(e -> e.setEstado(mensaje.estado()))
                .flatMap(this.repository::save)
                .then();
    }

    private Mono<OrdenPago> add(OrdenPagoDto dto, boolean esExitoso){
        var entity = EntityDtoMapper.toOrdenPago(dto);
        entity.setExito(esExitoso);
        return this.repository.save(entity);
    }

}
