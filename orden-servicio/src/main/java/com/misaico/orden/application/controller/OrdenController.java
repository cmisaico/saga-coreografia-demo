package com.misaico.orden.application.controller;

import com.misaico.orden.common.dto.OrdenCompraDto;
import com.misaico.orden.common.dto.OrdenCreaRequest;
import com.misaico.orden.common.dto.OrdenDetalle;
import com.misaico.orden.common.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("orden")
public class OrdenController {

    private final OrdenService service;

    @PostMapping
    public Mono<ResponseEntity<OrdenCompraDto>> colocarOrden(@RequestBody Mono<OrdenCreaRequest> mono){
        return mono.flatMap(this.service::colocarOrden)
                .map(ResponseEntity.accepted()::body);
    }

    @GetMapping("todo")
    public Flux<OrdenCompraDto> obtenerTodasOrdenes(){
        return this.service.obtenerTodasOrdenes();
    }

    @GetMapping("{ordenId}")
    public Mono<OrdenDetalle> obtenerOrdenDetalle(@PathVariable UUID ordenId){
        return this.service.obtenerOrdenDetalle(ordenId);
    }

}
