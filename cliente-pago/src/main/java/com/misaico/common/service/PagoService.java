package com.misaico.common.service;

import com.misaico.common.dto.PagoDto;
import com.misaico.common.dto.PagoProcessRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PagoService {

    Mono<PagoDto> procesar(PagoProcessRequest request);

    Mono<PagoDto> reembolsar(UUID ordenId);

}
