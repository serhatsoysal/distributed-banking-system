package com.banking.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionEventDto(
    String transactionId,
    String type,
    BigDecimal amount,
    String fromAccount,
    String toAccount,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp
) {}

