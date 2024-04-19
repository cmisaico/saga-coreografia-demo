package com.misaico.common.utils;

import reactor.kafka.receiver.ReceiverOffset;

public record Record<T>(String key, T mensaje, ReceiverOffset acknoledgement) {
}
