package com.banking.notification.service;

import com.banking.notification.config.NotificationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationProperties properties;

    public NotificationService(NotificationProperties properties) {
        this.properties = properties;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        if (properties.isEmailEnabled()) {
            logger.info("Sending email to: {} with subject: {}", to, subject);
            logger.info("Email body: {}", body);
        }
    }

    @Async
    public void sendSms(String phoneNumber, String message) {
        if (properties.isSmsEnabled()) {
            logger.info("Sending SMS to: {} with message: {}", phoneNumber, message);
        }
    }
}

