package com.banking.account.messaging;

import com.banking.account.domain.Account;
import com.banking.shared.constant.KafkaTopics;
import com.banking.shared.dto.AccountEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountEventProducer {

    private final KafkaTemplate<String, AccountEventDto> kafkaTemplate;

    public AccountEventProducer(KafkaTemplate<String, AccountEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAccountCreated(Account account) {
        AccountEventDto event = new AccountEventDto(
            UUID.randomUUID().toString(),
            "ACCOUNT_CREATED",
            account.getId(),
            account.getAccountNumber(),
            account.getCustomer().getId(),
            account.getBalance(),
            LocalDateTime.now()
        );
        kafkaTemplate.send(KafkaTopics.ACCOUNT_EVENTS, event);
    }

    public void publishAccountUpdated(Account account) {
        AccountEventDto event = new AccountEventDto(
            UUID.randomUUID().toString(),
            "ACCOUNT_UPDATED",
            account.getId(),
            account.getAccountNumber(),
            account.getCustomer().getId(),
            account.getBalance(),
            LocalDateTime.now()
        );
        kafkaTemplate.send(KafkaTopics.ACCOUNT_EVENTS, event);
    }
}

