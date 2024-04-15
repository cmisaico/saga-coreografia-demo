package com.misaico.envio.application.entity;

import com.misaico.events.envio.EnvioEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    private UUID id;
    private UUID ordenId;
    private Integer productoId;
    private Integer clienteId;
    private Integer cantidad;
    private Instant fechaEnvio;
    private EnvioEstado estado;
}
