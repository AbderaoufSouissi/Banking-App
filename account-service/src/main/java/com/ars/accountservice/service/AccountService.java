package com.ars.accountservice.service;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.domain.Transaction;
import com.ars.accountservice.request.CreateAccountRequest;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createAccount(CreateAccountRequest request);
    Account getAccountById(String id);
    Transaction credit(String accountId, double amount);
    Transaction debit(String accountId, double amount);
    List<Account> getAllAccounts();
}
