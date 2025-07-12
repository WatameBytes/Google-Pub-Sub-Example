package com.app.service;

import com.app.models.OrderLogMessage;
import com.business.gcp.GenericPubSubPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderLogService {

    private final GenericPubSubPublisher publisher;

    public String publish(OrderLogMessage msg) {
        try {
            String messageId = publisher.publish(msg).join();
            log.info("✓ Successfully published log message, id={}", messageId);
            return messageId;
        } catch (Exception e) {
            log.error("✗ Failed to publish log message", e);
            return "FAILED_PUBLISH";
        }
    }
}
