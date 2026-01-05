package com.banking.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTransactionRequest(
    @NotBlank(message = "Account number is required")
    String accountNumber,
    
    @NotNull(message = "Transaction type is required")
    String type,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,
    
    @NotBlank(message = "Currency is required")
    String currency,
    
    String fromAccount,
    String toAccount,
    String description
) {}

