package com.app.order;

import com.business.gcp.GenericPubSubPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingService {

    private final GenericPubSubPublisher pubSub;

    /** synchronous – waits for Pub/Sub to return the message-ID */
    public OrderResponse handle(OrderRequest req) {
        String normalized = req.getCustomerName().trim().toUpperCase();

        String messageId = null;
        try {
            messageId = pubSub.publish(req).join();
            log.info("Published order backup, id={}", messageId);
        } catch (Exception ex) {
            log.error("Pub/Sub publish failed", ex);
        }
        return new OrderResponse(normalized, messageId);
    }

    /** asynchronous – returns immediately */
    public OrderResponse publishAsync(OrderRequest req) {
        String normalized = req.getCustomerName().trim().toUpperCase();

        pubSub.publish(req).whenComplete((id, ex) -> {
            if (ex == null) {
                log.info("✓ async publish ok, id={}", id);
            } else {
                log.error("✗ async publish failed", ex);
            }
        });
        return new OrderResponse(normalized, null);
    }
}
