package com.banking.account.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank(message = "From account is required")
    String fromAccountNumber,
    
    @NotBlank(message = "To account is required")
    String toAccountNumber,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "1000000", message = "Amount exceeds maximum transfer limit")
    BigDecimal amount,
    
    String description
) {}

