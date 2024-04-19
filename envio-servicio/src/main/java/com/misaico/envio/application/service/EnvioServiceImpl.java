package com.misaico.envio.application.service;


import com.misaico.envio.application.entity.Envio;
import com.misaico.envio.application.mapper.EntityDtoMapper;
import com.misaico.envio.application.repository.EnvioRepository;
import com.misaico.envio.common.dto.EnvioDto;
import com.misaico.envio.common.dto.HorarioRequest;
import com.misaico.envio.common.service.EnvioService;
import com.misaico.common.events.envio.EnvioEstado;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private static final Logger log = LoggerFactory.getLogger(EnvioServiceImpl.class);
    private final EnvioRepository envioRepository;

    @Override
    public Mono<Void> agregarEnvio(HorarioRequest request) {
        return null;
    }

    private Mono<Void> add(HorarioRequest request){
        var envio = EntityDtoMapper.toEnvio(request);
        envio.setEstado(EnvioEstado.PENDIENTE);
        return this.envioRepository.save(envio)
                .then();
    }

    @Override
    public Mono<Void> cancelar(UUID ordenId) {
        return this.envioRepository.deleteByOrdenId(ordenId);
    }

    @Override
    public Mono<EnvioDto> programar(UUID ordenId) {
        return this.envioRepository.findByOrdenIdAndEstado(ordenId, EnvioEstado.PENDIENTE)
                .flatMap(this::programar);
    }

    private Mono<EnvioDto> programar(Envio envio){
        envio.setFechaEnvio(Instant.now().plus(Duration.ofDays(3)));
        envio.setEstado(EnvioEstado.PROGRAMADO);
        return this.envioRepository.save(envio)
                .map(EntityDtoMapper::toDto);
    }
}
