package com.banking.account;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@TestConfiguration
public class TestConfig {

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;
}

