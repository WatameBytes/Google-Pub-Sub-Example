package com.app.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProcessingService service;

    /** blocking endpoint */
    @PostMapping
    public ResponseEntity<OrderResponse> sync(@Valid @RequestBody OrderRequest req) {
        return ResponseEntity.ok(service.handle(req));
    }

    /** fire-and-forget endpoint */
    @PostMapping("/async")
    public ResponseEntity<OrderResponse> async(@Valid @RequestBody OrderRequest req) {
        return ResponseEntity.accepted().body(service.publishAsync(req));
    }
}
