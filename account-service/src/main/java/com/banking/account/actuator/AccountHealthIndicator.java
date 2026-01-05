package com.banking.account.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.banking.account.repository.AccountRepository;

@Component
public class AccountHealthIndicator implements HealthIndicator {

    private final AccountRepository accountRepository;

    public AccountHealthIndicator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Health health() {
        try {
            long count = accountRepository.count();
            return Health.up()
                .withDetail("totalAccounts", count)
                .withDetail("status", "Database connection healthy")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}

