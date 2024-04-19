package com.misaico.common.demo;

import com.misaico.common.events.orden.OrdenEvento;

import java.util.UUID;

public class MensajeHandlerDemo {

    public static void main(String[] args) {

        var event = OrdenEvento.OrdenCancelada.builder().ordenId(UUID.randomUUID()).build();

        var result = MensajeHandler.<OrdenEvento, String>create(event)
                .onMessage(OrdenEvento.OrdenCreada.class, e -> "created")
                .onMessage(OrdenEvento.OrdenCancelada.class, e -> "cancelled")
                .onMessage(OrdenEvento.OrdenCompletada.class, e -> "completed")
                .handle();

        System.out.println(result);

    }
}
