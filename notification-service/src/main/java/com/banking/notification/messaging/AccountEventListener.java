package com.banking.notification.messaging;

import com.banking.notification.service.NotificationService;
import com.banking.shared.constant.KafkaTopics;
import com.banking.shared.dto.AccountEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class AccountEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AccountEventListener.class);
    private final NotificationService notificationService;

    public AccountEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = KafkaTopics.ACCOUNT_EVENTS, groupId = "notification-group")
    public void handleAccountEvent(@Payload AccountEventDto event,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("Received account event: {} from topic: {}", event.eventType(), topic);

        String subject = "Account " + event.eventType();
        String body = String.format(
            "Account Number: %s\nCustomer ID: %d\nBalance: %s\nTimestamp: %s",
            event.accountNumber(),
            event.customerId(),
            event.balance(),
            event.timestamp()
        );

        notificationService.sendEmail("customer@example.com", subject, body);
    }
}

