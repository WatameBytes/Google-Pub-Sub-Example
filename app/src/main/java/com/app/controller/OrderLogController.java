package com.app.controller;

import com.app.models.OrderLogMessage;
import com.app.service.OrderLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class OrderLogController {

    private final OrderLogService orderLogService;

    @PostMapping
    public ResponseEntity<String> publishLog(@RequestBody OrderLogMessage message) {
        String messageId = orderLogService.publish(message);
        return ResponseEntity.ok("Published to PubSub with ID: " + messageId);
    }
}
