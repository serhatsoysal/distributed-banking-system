package com.banking.transaction.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.banking.transaction.domain.Transaction;
import com.banking.transaction.domain.TransactionStatus;
import com.banking.transaction.domain.TransactionType;
import com.banking.transaction.dto.CreateTransactionRequest;
import com.banking.transaction.dto.TransactionResponse;
import com.banking.transaction.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Mono<TransactionResponse> createTransaction(CreateTransactionRequest request) {
        Transaction transaction = new Transaction(
            request.accountNumber(),
            TransactionType.valueOf(request.type().toUpperCase()),
            request.amount(),
            request.currency(),
            request.description()
        );
        transaction.setFromAccount(request.fromAccount());
        transaction.setToAccount(request.toAccount());
        transaction.setStatus(TransactionStatus.COMPLETED);
        
        return transactionRepository.save(transaction)
            .map(this::mapToResponse);
    }

    public Mono<TransactionResponse> getTransactionById(String id) {
        return transactionRepository.findById(id)
            .map(this::mapToResponse);
    }

    public Flux<TransactionResponse> getTransactionsByAccount(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber)
            .map(this::mapToResponse);
    }

    public Flux<TransactionResponse> getTransactionsByAccountAndDateRange(
            String accountNumber, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByAccountNumberAndDateRange(accountNumber, start, end)
            .map(this::mapToResponse);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId(),
            transaction.getAccountNumber(),
            transaction.getType().name(),
            transaction.getAmount(),
            transaction.getCurrency(),
            transaction.getFromAccount(),
            transaction.getToAccount(),
            transaction.getDescription(),
            transaction.getStatus().name(),
            transaction.getCreatedAt()
        );
    }
}

