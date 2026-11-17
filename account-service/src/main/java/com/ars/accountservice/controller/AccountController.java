package com.ars.accountservice.controller;

import com.ars.accountservice.domain.Account;
import com.ars.accountservice.domain.Transaction;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;
import com.ars.accountservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @PostMapping("/credit/{accountId}")
    public ResponseEntity<Transaction> credit(@PathVariable String accountId, @RequestBody @Valid CreateTransactionRequest request) {
        return new ResponseEntity<>(accountService.credit(accountId, request), HttpStatus.OK);

    }

    @PostMapping("/debit/{accountId}")
    public ResponseEntity<Transaction> debit(@PathVariable String accountId, @RequestBody @Valid CreateTransactionRequest request) {
        return new ResponseEntity<>(accountService.debit(accountId, request), HttpStatus.OK);
    }

    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }


}
