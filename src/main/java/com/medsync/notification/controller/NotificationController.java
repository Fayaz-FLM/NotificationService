package com.medsync.notification.controller;

import com.medsync.notification.dto.EmailRequest;
import com.medsync.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {

        notificationService.sendEmail(request);

        return ResponseEntity.ok("Email sent successfully");
    }
}