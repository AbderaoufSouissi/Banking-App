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
import com.ars.accountservice.response.AccountResponse;
import com.ars.accountservice.response.TransactionResponse;
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
    public AccountResponse createAccount(CreateAccountRequest request) {
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

        return toAccountResponse(createdAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(String id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id: " + id + " not found")
        );
        return toAccountResponse(account);
    }

    @Override
    public TransactionResponse credit(String accountId, CreateTransactionRequest request) {
        Account account = findAccountById(accountId);
        account.setBalance(account.getBalance() + request.amount());
        accountRepository.save(account);

        var transaction = Transaction.builder()
                .dateTime(now())
                .amount(request.amount())
                .type(TransactionType.CREDIT)
                .account(account)
                .build();

        var savedTransaction = transactionRepository.save(transaction);
        return toTransactionResponse(savedTransaction);
    }


    @Override
    public TransactionResponse debit(String accountId, CreateTransactionRequest request) {
        Account account = findAccountById(accountId);
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

        var savedTransaction = transactionRepository.save(transaction);
        return toTransactionResponse(savedTransaction);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::toAccountResponse)
                .toList();
    }


    private Account findAccountById(String id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id: " + id + " not found")
        );
    }

    private AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getBalance(),
                account.getType(),
                account.getStatus(),
                account.getCurrency(),
                account.getCustomerId(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDateTime(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getAccount().getId()
        );
    }
}
