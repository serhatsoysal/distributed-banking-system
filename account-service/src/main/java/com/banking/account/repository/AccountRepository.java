package com.banking.account.repository;

import com.banking.account.domain.Account;
import com.banking.account.domain.AccountStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = {"customer"})
    @Query("SELECT a FROM BankAccount a WHERE a.id = :id")
    Optional<Account> findByIdWithCustomer(@Param("id") Long id);

    Optional<Account> findByAccountNumber(String accountNumber);

    @EntityGraph(attributePaths = {"customer"})
    List<Account> findByCustomerId(Long customerId);

    @Query(value = "SELECT * FROM accounts WHERE customer_id = :customerId AND status = :status", nativeQuery = true)
    List<Account> findActiveAccountsByCustomer(@Param("customerId") Long customerId, @Param("status") String status);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByStatus(AccountStatus status);
}

