package com.misaico.orden;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(properties = {
        "logging.level.root=ERROR",
        "logging.level.com.misaico*=INFO",
        "spring.cloud.stream.kafka.binder.configuration.auto.offset.reset=earliest",
})
@EmbeddedKafka(
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public abstract class AbstractIntegrationTest {


}
