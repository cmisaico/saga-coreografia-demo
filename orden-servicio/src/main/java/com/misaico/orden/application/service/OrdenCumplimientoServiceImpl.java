package com.misaico.orden.application.service;

import com.misaico.common.events.orden.OrdenEstado;
import com.misaico.orden.application.mapper.EntityDtoMapper;
import com.misaico.orden.application.repository.OrdenCompraRepository;
import com.misaico.orden.common.dto.OrdenCompraDto;
import com.misaico.orden.common.service.OrdenCumplimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdenCumplimientoServiceImpl implements OrdenCumplimientoService {
    private final OrdenCompraRepository repository;
    @Override
    public Mono<OrdenCompraDto> completar(UUID ordenId) {
        return this.repository.getWhenOrdenComponenteCompletado(ordenId)
                .doOnNext(e -> e.setEstado(OrdenEstado.COMPLETADO))
                .flatMap(this.repository::save)
                .map(EntityDtoMapper::toOrdenCompraDto);
    }

    @Override
    public Mono<OrdenCompraDto> cancelar(UUID ordenId) {
        return this.repository.findByOrdenIdAndEstado(ordenId, OrdenEstado.COMPLETADO)
                .doOnNext(e -> e.setEstado(OrdenEstado.CANCELADO))
                .flatMap(this.repository::save)
                .map(EntityDtoMapper::toOrdenCompraDto);
    }
}
