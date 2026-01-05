package com.banking.account.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateAccountRequest(
    @NotNull(message = "Customer ID is required", groups = CreateValidation.class)
    Long customerId,
    
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "Account number must be 10-20 digits")
    String accountNumber,
    
    @NotNull(message = "Account type is required")
    String type,
    
    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    BigDecimal initialBalance,
    
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    String currency
) {
    public interface CreateValidation {}
    public interface UpdateValidation {}
}

