package com.banking.account.service;

import com.banking.account.domain.*;
import com.banking.account.dto.*;
import com.banking.account.repository.AccountRepository;
import com.banking.account.repository.CustomerRepository;
import com.banking.account.messaging.AccountEventProducer;
import com.banking.shared.exception.BusinessException;
import com.banking.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountEventProducer eventProducer;

    public AccountService(AccountRepository accountRepository, 
                         CustomerRepository customerRepository,
                         AccountEventProducer eventProducer) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional(rollbackFor = Exception.class)
    public AccountResponse createAccount(CreateAccountRequest request) {
        if (accountRepository.existsByAccountNumber(request.accountNumber())) {
            throw new BusinessException("Account number already exists");
        }

        Customer customer = customerRepository.findById(request.customerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.customerId()));

        AccountType accountType = AccountType.valueOf(request.type().toUpperCase());
        
        Account account = new Account(
            request.accountNumber(),
            customer,
            accountType,
            request.initialBalance(),
            request.currency()
        );

        Account saved = accountRepository.save(account);
        eventProducer.publishAccountCreated(saved);
        
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findByIdWithCustomer(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return mapToResponse(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        return mapToResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }

        account.deposit(amount);
        accountRepository.save(account);
        eventProducer.publishAccountUpdated(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public void withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Withdrawal amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient funds");
        }

        account.withdraw(amount);
        accountRepository.save(account);
        eventProducer.publishAccountUpdated(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public void closeAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("Cannot close account with non-zero balance");
        }

        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getCustomer().getId(),
            account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),
            account.getType().name(),
            account.getBalance(),
            account.getCurrency(),
            account.getStatus().name(),
            account.getCreatedAt()
        );
    }
}

