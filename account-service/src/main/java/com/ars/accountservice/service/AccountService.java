package com.ars.accountservice.service;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.request.CreateAccountRequest;

public interface AccountService {
    Account createAccount(CreateAccountRequest request);
}
