package com.banking.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
    Long id,
    String accountNumber,
    Long customerId,
    String customerName,
    String type,
    BigDecimal balance,
    String currency,
    String status,
    LocalDateTime createdAt
) {}

