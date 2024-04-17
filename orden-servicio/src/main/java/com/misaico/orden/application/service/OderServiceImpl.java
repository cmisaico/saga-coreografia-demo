package com.misaico.orden.application.service;

import com.misaico.events.envio.EnvioEstado;
import com.misaico.orden.application.mapper.EntityDtoMapper;
import com.misaico.orden.application.repository.OrdenCompraRepository;
import com.misaico.orden.common.dto.OrdenCompraDto;
import com.misaico.orden.common.dto.OrdenCreaRequest;
import com.misaico.orden.common.dto.OrdenDetalle;
import com.misaico.orden.common.service.OrdenEventoListener;
import com.misaico.orden.common.service.OrdenService;
import com.misaico.orden.common.service.inventario.InventarioComponenteFetcher;
import com.misaico.orden.common.service.pago.PagoComponenteFetcher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OderServiceImpl implements OrdenService {

    private static final Logger log = LoggerFactory.getLogger(OderServiceImpl.class);
    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenEventoListener eventoListener;
    private final PagoComponenteFetcher pagoComponenteFetcher;
    private final InventarioComponenteFetcher inventarioComponenteFetcher;

    @Override
    public Mono<OrdenCompraDto> colocarOrden(OrdenCreaRequest request) {
        var entity = EntityDtoMapper.toOrdenCompra(request);
        return this.ordenCompraRepository.save(entity)
                .map(EntityDtoMapper::toOrdenCompraDto)
                .doOnNext(eventoListener::emitirOrdenCreado);
    }

    @Override
    public Flux<OrdenCompraDto> obtenerTodasOrdenes() {
        return this.ordenCompraRepository.findAll()
                .map(EntityDtoMapper::toOrdenCompraDto);
    }

    @Override
    public Mono<OrdenDetalle> obtenerOrdenDetalle(UUID ordenId) {
        return this.ordenCompraRepository.findById(ordenId)
                .map(EntityDtoMapper::toOrdenCompraDto)
                .flatMap(dto -> this.pagoComponenteFetcher.obtenerComponente(ordenId)
                        .zipWith(this.inventarioComponenteFetcher.obtenerComponente(ordenId))
                        .map(t -> EntityDtoMapper.toOrdenDetalle(dto, t.getT1(), t.getT2()))
                );
    }
}
