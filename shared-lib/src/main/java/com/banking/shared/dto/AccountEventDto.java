package com.banking.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountEventDto(
    String eventId,
    String eventType,
    Long accountId,
    String accountNumber,
    Long customerId,
    BigDecimal balance,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp
) {}

