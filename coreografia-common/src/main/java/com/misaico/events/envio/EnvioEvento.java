package com.misaico.events.envio;

import com.misaico.events.DomainEvent;
import com.misaico.events.OrdenSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public interface EnvioEvento extends DomainEvent, OrdenSaga {

    @Builder
    record EnvioProgramado(UUID ordenId,
                           UUID envioId,
                           Instant entregaEsperada,
                           Instant createdAt) implements EnvioEvento {
    }


}
