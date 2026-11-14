package com.ars.accountservice.service.impl;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.domain.AccountStatus;
import com.ars.accountservice.domain.Transaction;
import com.ars.accountservice.domain.TransactionType;
import com.ars.accountservice.repository.AccountRepository;
import com.ars.accountservice.repository.TransactionRepository;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;



    @Override
    public Account createAccount(CreateAccountRequest request) {
        var account = Account.builder()
                .balance(request.balance())
                .type(request.accountType())
                .status(AccountStatus.CREATED)
                .currency(request.currency())
                .customerId(request.customerId())
                .build();

        var createdAccount = accountRepository.save(account);

        var transaction = Transaction.builder()
                .dateTime(now())
                .amount(request.balance())
                .type(TransactionType.CREDIT)
                .account(createdAccount)
                .build();

        transactionRepository.save(transaction);


        return createdAccount;
    }
}
