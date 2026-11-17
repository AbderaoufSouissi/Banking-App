package com.ars.accountservice.mcp;

import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;
import com.ars.accountservice.response.AccountResponse;
import com.ars.accountservice.response.TransactionResponse;
import com.ars.accountservice.service.AccountService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMcpTools {

    private final AccountService accountService;

    public AccountMcpTools(AccountService accountService) {
        this.accountService = accountService;
    }

    @McpTool(name = "create_account", description = "Opens a new bank account with the specified type, currency, initial balance, and customer ID. This operation is not idempotent — calling it multiple times with the same parameters will create duplicate accounts, so do not retry blindly on failure.")
    public AccountResponse createAccount(
            @McpToolParam(description = "Request containing account details: initial balance (>= 0), account type (CURRENT_ACCOUNT or SAVINGS_ACCOUNT), currency (3-letter ISO 4217 code), and customer ID (> 0). All fields are required.", required = true)
            CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @McpTool(name = "get_account_by_id", description = "Retrieves the full details of a single account by its unique ID. Use this tool when the caller knows the exact account ID and needs that specific account's information. For browsing or listing multiple accounts, use list_accounts instead.")
    public AccountResponse getAccountById(
            @McpToolParam(description = "The unique UUID string identifying the account to retrieve.", required = true)
            String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Account ID must not be null or blank");
        }
        return accountService.getAccountById(id);
    }

    @McpTool(name = "list_accounts", description = "Returns a list of all accounts in the system for browsing and discovery. Use this tool when you need to see available accounts or search for one by inspecting the results. For looking up a known account ID directly, use get_account_by_id instead.")
    public List<AccountResponse> listAccounts() {
        return accountService.getAllAccounts();
    }

    @McpTool(name = "credit_account", description = "Adds funds to an existing account by its ID. This operation is not idempotent — calling it multiple times will add the amount each time, so do not retry blindly on failure. The amount must be positive.")
    public TransactionResponse creditAccount(
            @McpToolParam(description = "The unique UUID string identifying the account to credit.", required = true)
            String accountId,
            @McpToolParam(description = "Request containing the amount to add. The amount must be a positive number greater than zero.", required = true)
            CreateTransactionRequest request) {

        return accountService.credit(accountId, request);
    }

    @McpTool(name = "debit_account", description = "Removes funds from an existing account by its ID. This is a destructive write operation that is not idempotent — calling it multiple times will deduct the amount each time, so do not retry blindly on failure. The operation will fail if the account balance is insufficient to cover the requested amount. The amount must be positive.")
    public TransactionResponse debitAccount(
            @McpToolParam(description = "The unique UUID string identifying the account to debit.", required = true)
            String accountId,
            @McpToolParam(description = "Request containing the amount to remove. The amount must be a positive number greater than zero. The account must have sufficient balance.", required = true)
            CreateTransactionRequest request) {

        return accountService.debit(accountId, request);
    }


}
