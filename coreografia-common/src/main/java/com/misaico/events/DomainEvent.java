package com.misaico.events;

import java.time.Instant;

public interface DomainEvent {
    Instant createdAt();
}
