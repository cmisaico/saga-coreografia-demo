package com.misaico.orden.application.service;

import com.misaico.orden.application.entity.OrdenInventario;
import com.misaico.orden.application.entity.OrdenPago;
import com.misaico.orden.application.mapper.EntityDtoMapper;
import com.misaico.orden.application.repository.OrdenInventarioRepository;
import com.misaico.orden.common.dto.OrdenInventarioDto;
import com.misaico.orden.common.dto.OrdenPagoDto;
import com.misaico.orden.common.service.inventario.InventarioComponenteEstadoListener;
import com.misaico.orden.common.service.inventario.InventarioComponenteFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventarioComponenteService implements InventarioComponenteFetcher, InventarioComponenteEstadoListener {

    private static final OrdenInventarioDto DEFAULT = OrdenInventarioDto.builder().build();
    private final OrdenInventarioRepository repository;

    @Override
    public Mono<OrdenInventarioDto> obtenerComponente(UUID ordenId) {
        return this.repository.findByOrdenId(ordenId)
                .map(EntityDtoMapper::toOrdenInventarioDto)
                .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrdenInventarioDto mensaje) {
        return this.repository.findByOrdenId(mensaje.ordenId())
                .switchIfEmpty(Mono.defer(() -> this.add(mensaje, true)))
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrdenInventarioDto mensaje) {
        return this.repository.findByOrdenId(mensaje.ordenId())
                .switchIfEmpty(Mono.defer(() -> this.add(mensaje, false)))
                .then();
    }

    @Override
    public Mono<Void> onRollback(OrdenInventarioDto mensaje) {
        return this.repository.findByOrdenId(mensaje.ordenId())
                .doOnNext(e -> e.setEstado(mensaje.estado()))
                .flatMap(this.repository::save)
                .then();
    }

    private Mono<OrdenInventario> add(OrdenInventarioDto dto, boolean esExitoso){
        var entity = EntityDtoMapper.toOrdenInventario(dto);
        entity.setExito(esExitoso);
        return this.repository.save(entity);
    }


}
