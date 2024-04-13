package com.misaico.utils;

import reactor.kafka.receiver.ReceiverOffset;

public record Record<T>(String key, T mensaje, ReceiverOffset acknoledgement) {
}
