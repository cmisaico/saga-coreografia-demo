package com.misaico.common.events.envio;

import com.misaico.common.events.DomainEvent;
import com.misaico.common.events.OrdenSaga;
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
