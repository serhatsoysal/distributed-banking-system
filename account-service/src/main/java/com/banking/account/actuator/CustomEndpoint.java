package com.banking.account.actuator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import com.banking.account.domain.AccountStatus;
import com.banking.account.repository.AccountRepository;

@Component
@Endpoint(id = "accounts-stats")
public class CustomEndpoint {

    private final AccountRepository accountRepository;

    public CustomEndpoint(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @ReadOperation
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAccounts", accountRepository.count());
        stats.put("activeAccounts", accountRepository.findByStatus(AccountStatus.ACTIVE).size());
        stats.put("closedAccounts", accountRepository.findByStatus(AccountStatus.CLOSED).size());
        return stats;
    }

    @WriteOperation
    public String refresh() {
        return "Statistics refreshed at " + System.currentTimeMillis();
    }
}

