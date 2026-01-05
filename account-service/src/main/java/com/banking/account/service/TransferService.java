package com.banking.account.service;

import com.banking.account.annotation.LogExecutionTime;
import com.banking.account.domain.Account;
import com.banking.account.dto.TransferRequest;
import com.banking.account.repository.AccountRepository;
import com.banking.shared.exception.InsufficientFundsException;
import com.banking.shared.exception.ResourceNotFoundException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @LogExecutionTime
    @RateLimiter(name = "default")
    @Bulkhead(name = "default")
    @CircuitBreaker(name = "default", fallbackMethod = "transferFallback")
    @TimeLimiter(name = "default")
    @Retry(name = "default")
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferRequest request) {
        Account fromAccount = accountRepository.findByAccountNumber(request.fromAccountNumber())
            .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account toAccount = accountRepository.findByAccountNumber(request.toAccountNumber())
            .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in source account");
        }

        fromAccount.withdraw(request.amount());
        toAccount.deposit(request.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public void transferFallback(TransferRequest request, Exception ex) {
        throw new RuntimeException("Transfer service temporarily unavailable. Please try again later.");
    }
}

