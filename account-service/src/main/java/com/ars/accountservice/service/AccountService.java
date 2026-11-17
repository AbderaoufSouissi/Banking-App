package com.ars.accountservice.service;

import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;
import com.ars.accountservice.response.AccountResponse;
import com.ars.accountservice.response.TransactionResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    AccountResponse getAccountById(String id);
    TransactionResponse credit(String accountId, CreateTransactionRequest request);
    TransactionResponse debit(String accountId, CreateTransactionRequest request);
    List<AccountResponse> getAllAccounts();
}
