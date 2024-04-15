package com.misaico.pago.common.service;

import com.misaico.pago.common.dto.PagoDto;
import com.misaico.pago.common.dto.PagoProcessRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PagoService {

    Mono<PagoDto> procesar(PagoProcessRequest request);

    Mono<PagoDto> reembolsar(UUID ordenId);

}
