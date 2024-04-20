package com.misaico.envio;

import com.misaico.common.events.orden.OrdenEvento;

import java.time.Instant;
import java.util.UUID;

public class TestDataUtil {
    public static OrdenEvento.OrdenCreada crearOrdenCreadoEvento(int clienteId, int productoId, int precioUnidad, int cantidad) {
        return OrdenEvento.OrdenCreada.builder()
                .ordenId(UUID.randomUUID())
                .createdAt(Instant.now())
                .montoTotal(precioUnidad * cantidad)
                .precioUnidad(precioUnidad)
                .cantidad(cantidad)
                .clienteId(clienteId)
                .productoId(productoId)
                .build();
    }

    public static OrdenEvento.OrdenCancelada crearOrdenCanceladoEvento(UUID orderId) {
        return OrdenEvento.OrdenCancelada.builder()
                .ordenId(orderId)
                .createdAt(Instant.now())
                .build();
    }

    public static OrdenEvento.OrdenCompletada crearOrdenCompletadoEvento(UUID orderId) {
        return OrdenEvento.OrdenCompletada.builder()
                .ordenId(orderId)
                .createdAt(Instant.now())
                .build();
    }
}
