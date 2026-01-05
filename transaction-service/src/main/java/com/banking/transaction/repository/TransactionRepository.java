package com.banking.transaction.repository;

import com.banking.transaction.domain.Transaction;
import com.banking.transaction.domain.TransactionStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findByAccountNumber(String accountNumber);

    Flux<Transaction> findByAccountNumberAndStatus(String accountNumber, TransactionStatus status);

    @Query("{ 'accountNumber': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    Flux<Transaction> findByAccountNumberAndDateRange(String accountNumber, LocalDateTime start, LocalDateTime end);

    Mono<Long> countByAccountNumber(String accountNumber);
}

