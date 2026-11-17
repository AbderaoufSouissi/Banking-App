package com.ars.accountservice.controller;

import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;
import com.ars.accountservice.response.AccountResponse;
import com.ars.accountservice.response.TransactionResponse;
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
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable String id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @PostMapping("/credit/{accountId}")
    public ResponseEntity<TransactionResponse> credit(@PathVariable String accountId, @RequestBody @Valid CreateTransactionRequest request) {
        return new ResponseEntity<>(accountService.credit(accountId, request), HttpStatus.OK);

    }

    @PostMapping("/debit/{accountId}")
    public ResponseEntity<TransactionResponse> debit(@PathVariable String accountId, @RequestBody @Valid CreateTransactionRequest request) {
        return new ResponseEntity<>(accountService.debit(accountId, request), HttpStatus.OK);
    }

    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }


}
