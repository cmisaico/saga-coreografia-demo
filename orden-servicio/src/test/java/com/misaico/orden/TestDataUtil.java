package com.misaico.orden;

import com.misaico.orden.common.dto.OrdenCreaRequest;

public class TestDataUtil {
    public static OrdenCreaRequest toRequest(int clienteId, int productoId, int precioUnidad, int cantidad){
        return OrdenCreaRequest.builder()
                .precioUnidad(precioUnidad)
                .cantidad(cantidad)
                .clienteId(clienteId)
                .productoId(productoId)
                .build();
    }

//    public static OrdenEvento.OrdenCancelada crearOrdenCanceladoEvento(UUID ordenId){
//        return OrdenEvento.OrdenCancelada.builder()
//                .ordenId(ordenId)
//                .createdAt(Instant.now())
//                .build();
//    }
}
