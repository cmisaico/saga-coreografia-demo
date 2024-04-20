package com.misaico.envio.application.mapper;

import com.misaico.envio.application.entity.Envio;
import com.misaico.envio.common.dto.EnvioDto;
import com.misaico.envio.common.dto.HorarioRequest;

public class EntityDtoMapper {
    public static Envio toEnvio(HorarioRequest request){
        return Envio.builder()
                .clienteId(request.clienteId())
                .ordenId(request.ordenId())
                .productoId(request.productoId())
                .cantidad(request.cantidad())
                .build();
    }

    public static EnvioDto toDto(Envio envio){
        return EnvioDto.builder()
                .envioId(envio.getId())
                .clienteId(envio.getClienteId())
                .cantidad(envio.getCantidad())
                .productoId(envio.getProductoId())
                .ordenId(envio.getOrdenId())
                .estado(envio.getEstado())
                .fechaEnvio(envio.getFechaEnvio())
                .build();
    }

}


