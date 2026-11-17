package com.ars.accountservice.service.impl;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.domain.AccountStatus;
import com.ars.accountservice.domain.Transaction;
import com.ars.accountservice.domain.TransactionType;
import com.ars.accountservice.exception.AccountNotFoundException;
import com.ars.accountservice.exception.UnsufficientBalanceException;
import com.ars.accountservice.repository.AccountRepository;
import com.ars.accountservice.repository.TransactionRepository;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;
import com.ars.accountservice.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


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

    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(String id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id: " + id + " not found")
        );
    }

    @Override
    public Transaction credit(String accountId, CreateTransactionRequest request) {
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance() + request.amount());
        accountRepository.save(account);

        var transaction = Transaction.builder()
                .dateTime(now())
                .amount(request.amount())
                .type(TransactionType.CREDIT)
                .account(account)
                .build();

        return transactionRepository.save(transaction);
    }


    @Override
    public Transaction debit(String accountId, CreateTransactionRequest request) {
        Account account = getAccountById(accountId);
        double currentBalance = account.getBalance();
        if (currentBalance < request.amount()) throw new UnsufficientBalanceException("unsufficient balance");
        account.setBalance(account.getBalance() - request.amount());
        accountRepository.save(account);

        var transaction = Transaction.builder()
                .dateTime(now())
                .amount(request.amount())
                .type(TransactionType.DEBIT)
                .account(account)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
