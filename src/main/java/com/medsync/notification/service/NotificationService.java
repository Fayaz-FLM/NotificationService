package com.medsync.notification.service;

import com.medsync.notification.dto.EmailRequest;

public interface NotificationService {
    void sendEmail(EmailRequest request);
}