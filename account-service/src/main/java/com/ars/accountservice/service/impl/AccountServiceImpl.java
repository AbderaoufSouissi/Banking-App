package com.ars.accountservice.service.impl;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.domain.AccountStatus;
import com.ars.accountservice.repository.AccountRepository;
import com.ars.accountservice.repository.TransactionRepository;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        return createdAccount;
    }
}
