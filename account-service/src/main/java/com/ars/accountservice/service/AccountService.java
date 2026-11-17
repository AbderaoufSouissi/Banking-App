package com.ars.accountservice.service;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.domain.Transaction;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;

import java.util.List;

public interface AccountService {
    Account createAccount(CreateAccountRequest request);
    Account getAccountById(String id);
    Transaction credit(String accountId, CreateTransactionRequest request);
    Transaction debit(String accountId, CreateTransactionRequest request);
    List<Account> getAllAccounts();
}
