package com.banking.account.service;

import com.banking.account.domain.Customer;
import com.banking.account.dto.CreateCustomerRequest;
import com.banking.account.repository.CustomerRepository;
import com.banking.shared.exception.BusinessException;
import com.banking.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new BusinessException("Customer with this email already exists");
        }

        Customer customer = new Customer(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phoneNumber()
        );

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findByIdWithAccounts(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
    }
}

