package com.banking.account.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@ConfigurationProperties(prefix = "app.account")
@Validated
public class AppProperties {

    @Min(value = 0, message = "Minimum balance cannot be negative")
    private BigDecimal minBalance = BigDecimal.ZERO;

    @Max(value = 10000000, message = "Maximum balance exceeds system limit")
    private BigDecimal maxBalance = new BigDecimal("1000000");

    @NotBlank(message = "Default currency must be specified")
    private String defaultCurrency = "USD";

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}

