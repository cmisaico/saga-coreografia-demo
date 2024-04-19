package com.misaico.common.utils;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import reactor.kafka.receiver.ReceiverOffset;

public class MensajeConvertidor {

    public static <T> Record<T> toRecord(Message<T> mensaje) {
        var payload = mensaje.getPayload();
        var key = mensaje.getHeaders().get(KafkaHeaders.RECEIVED_KEY, String.class);
        var ack = mensaje.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset.class);
        return new Record<>(key, payload, ack);
    }
}
