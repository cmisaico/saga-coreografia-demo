package com.misaico.inventario.application.service;


import com.misaico.events.inventario.InventarioEstado;
import com.misaico.events.pago.PagoEstado;
import com.misaico.inventario.application.entity.InventarioOrden;
import com.misaico.inventario.application.entity.Producto;
import com.misaico.inventario.application.mapper.EntityDtoMapper;
import com.misaico.inventario.application.repository.InventarioRepository;
import com.misaico.inventario.application.repository.ProductoRepository;
import com.misaico.inventario.common.dto.InventarioDescontadoRequest;
import com.misaico.inventario.common.dto.InventarioOrdenDto;
import com.misaico.inventario.common.exception.AgotadoException;
import com.misaico.inventario.common.service.InventarioService;
import com.misaico.utils.DuplicadoEventoValidador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioServiceImpl.class);
    private static final Mono<Producto> AGOTADO = Mono.error(new AgotadoException());
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;


    @Override
    @Transactional
    public Mono<InventarioOrdenDto> descontar(InventarioDescontadoRequest request) {
        return DuplicadoEventoValidador.validar(
                this.inventarioRepository.existsByOrdenId(request.ordenId()),
                this.productoRepository.findById(request.productoId())
        )
                .filter(p -> p.getCantidadDisponible() >= request.cantidad())
                .switchIfEmpty(AGOTADO)
                .flatMap(p -> this.descontarInventario(p, request))
                .doOnNext(dto -> log.info("inventario descontado for {}", dto.ordenId()));
    }

    private Mono<InventarioOrdenDto> descontarInventario(Producto producto, InventarioDescontadoRequest request) {
        var inventarioOrden = EntityDtoMapper.toInventarioOrden(request);
        producto.setCantidadDisponible(producto.getCantidadDisponible() - request.cantidad());
        inventarioOrden.setEstado(InventarioEstado.DESCONTADO);
        return this.productoRepository.save(producto)
                .then(this.inventarioRepository.save(inventarioOrden))
                .map(EntityDtoMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<InventarioOrdenDto> restaurar(UUID ordenId) {
        return this.inventarioRepository.findByOrdenIdAndEstado(ordenId, InventarioEstado.DESCONTADO)
                .zipWhen(i -> this.productoRepository.findById(i.getProductoId()))
                .flatMap(t -> this.restaurar(t.getT1(), t.getT2()))
                .doOnNext(dto -> log.info("cantidad restaurada: {} para {}", dto.cantidad(), dto.ordenId()));
    }

    private Mono<InventarioOrdenDto> restaurar(InventarioOrden inventarioOrden, Producto producto){
        producto.setCantidadDisponible(producto.getCantidadDisponible() + inventarioOrden.getCantidad());
        inventarioOrden.setEstado(InventarioEstado.RESTAURADO);
        return this.productoRepository.save(producto)
                .then(this.inventarioRepository.save(inventarioOrden))
                .map(EntityDtoMapper::toDto);
    }

}
