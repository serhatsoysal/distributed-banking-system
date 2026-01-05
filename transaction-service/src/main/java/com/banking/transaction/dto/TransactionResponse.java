package com.banking.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
    String id,
    String accountNumber,
    String type,
    BigDecimal amount,
    String currency,
    String fromAccount,
    String toAccount,
    String description,
    String status,
    LocalDateTime createdAt
) {}

