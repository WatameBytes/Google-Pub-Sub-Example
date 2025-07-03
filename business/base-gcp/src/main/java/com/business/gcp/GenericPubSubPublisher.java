package com.business.gcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericPubSubPublisher {

    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;

    public <T> CompletableFuture<String> publish(T payload) {

        PubSubRoute route = payload.getClass().getAnnotation(PubSubRoute.class);
        if (route == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Missing @PubSubRoute on " + payload.getClass().getName()));
        }

        String topic  = route.topic();
        String entity = route.entity().isBlank()
                ? payload.getClass().getSimpleName()
                : route.entity();

        try {
            String json = objectMapper.writeValueAsString(payload);
            log.debug("Publishing {} to '{}': {}", entity, topic, json);

            return pubSubTemplate.publish(topic, json)
                    .whenComplete((id, ex) -> {
                        if (ex == null) {
                            log.info("{} published, ID={}", entity, id);
                        } else {
                            log.error("Failed to publish {}", entity, ex);
                        }
                    });

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
