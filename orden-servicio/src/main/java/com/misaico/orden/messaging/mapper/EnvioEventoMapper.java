package com.misaico.orden.messaging.mapper;

import com.misaico.events.envio.EnvioEvento;
import com.misaico.orden.common.dto.OrdenEnvioProgramado;

public class EnvioEventoMapper {

    public static OrdenEnvioProgramado toDto(EnvioEvento.EnvioProgramado evento){
        return OrdenEnvioProgramado.builder()
                .ordenId(evento.ordenId())
                .fechaEnvio(evento.entregaEsperada())
                .build();
    }

}
