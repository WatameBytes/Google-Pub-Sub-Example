package com.app.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String processedCustomer;   // result of core logic
    private String correlationId;       // Pub/Sub message-ID (nullable)
}
