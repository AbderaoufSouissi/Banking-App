package com.ars.accountservice.service.impl;

import com.ars.accountservice.domain.AccountStatus;
import com.ars.accountservice.domain.AccountType;
import com.ars.accountservice.domain.TransactionType;
import com.ars.accountservice.repository.AccountRepository;
import com.ars.accountservice.repository.TransactionRepository;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.response.AccountResponse;
import com.ars.accountservice.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class AccountServiceImplIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void createAccountPersistsAccountAndInitialCreditTransaction() {
        var request = new CreateAccountRequest(250.00, AccountType.CURRENT_ACCOUNT, "EUR", 42L);

        AccountResponse response = accountService.createAccount(request);

        assertThat(response.id()).isNotBlank();
        assertThat(response.balance()).isEqualTo(250.00);
        assertThat(response.type()).isEqualTo(AccountType.CURRENT_ACCOUNT);
        assertThat(response.status()).isEqualTo(AccountStatus.CREATED);
        assertThat(response.currency()).isEqualTo("EUR");
        assertThat(response.customerId()).isEqualTo(42L);

        var transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(1);
        assertThat(transactions.getFirst().getAmount()).isEqualTo(250.00);
        assertThat(transactions.getFirst().getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(transactions.getFirst().getAccount().getId()).isEqualTo(response.id());
    }
}
