package com.misaico.orden.messaging.mapper;

import com.misaico.envio.common.dto.EnvioDto;
import com.misaico.envio.common.dto.HorarioRequest;
import com.misaico.events.envio.EnvioEvento;
import com.misaico.events.orden.OrdenEvento;

import java.time.Instant;

public class MensajeDtoMapper {

    public static HorarioRequest toHorarioRequest(OrdenEvento.OrdenCreada evento){
        return HorarioRequest.builder()
                .clienteId(evento.clienteId())
                .productoId(evento.productoId())
                .cantidad(evento.cantidad())
                .ordenId(evento.ordenId())
                .build();
    }

    public static EnvioEvento toEnvioProgramadoEvento(EnvioDto dto){
        return EnvioEvento.EnvioProgramado.builder()
                .envioId(dto.envioId())
                .ordenId(dto.ordenId())
                .createdAt(Instant.now())
                .entregaEsperada(dto.fechaEnvio())
                .build();
    }

}
