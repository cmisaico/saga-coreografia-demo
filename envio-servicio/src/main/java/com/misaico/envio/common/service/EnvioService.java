package com.misaico.envio.common.service;

import com.misaico.envio.common.dto.EnvioDto;
import com.misaico.envio.common.dto.HorarioRequest;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface EnvioService {

    Mono<Void> agregarEnvio(HorarioRequest request);

    Mono<Void> cancelar(UUID ordenId);

    Mono<EnvioDto> programar(UUID ordenId);

}
