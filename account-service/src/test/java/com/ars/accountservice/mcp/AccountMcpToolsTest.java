package com.ars.accountservice.mcp;

import com.ars.accountservice.domain.AccountStatus;
import com.ars.accountservice.domain.AccountType;
import com.ars.accountservice.domain.TransactionType;
import com.ars.accountservice.exception.AccountNotFoundException;
import com.ars.accountservice.exception.UnsufficientBalanceException;
import com.ars.accountservice.request.CreateAccountRequest;
import com.ars.accountservice.request.CreateTransactionRequest;
import com.ars.accountservice.response.AccountResponse;
import com.ars.accountservice.response.TransactionResponse;
import com.ars.accountservice.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountMcpToolsTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountMcpTools accountMcpTools;

    private static final String VALID_ACCOUNT_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 12, 0, 0);

    private final AccountResponse sampleAccountResponse = new AccountResponse(
            VALID_ACCOUNT_ID,
            1000.0,
            AccountType.CURRENT_ACCOUNT,
            AccountStatus.ACTIVE,
            "USD",
            42L,
            NOW,
            NOW
    );

    private final TransactionResponse sampleTransactionResponse = new TransactionResponse(
            1L,
            NOW,
            100.0,
            TransactionType.CREDIT,
            VALID_ACCOUNT_ID
    );

    @Test
    void createAccount_delegatesToServiceAndReturnsAccountResponse() {
        var request = new CreateAccountRequest(250.0, AccountType.SAVINGS_ACCOUNT, "EUR", 10L);
        when(accountService.createAccount(request)).thenReturn(sampleAccountResponse);

        AccountResponse result = accountMcpTools.createAccount(request);

        assertThat(result).isEqualTo(sampleAccountResponse);
        assertThat(result.id()).isEqualTo(VALID_ACCOUNT_ID);
        assertThat(result.type()).isEqualTo(AccountType.CURRENT_ACCOUNT);
        assertThat(result.currency()).isEqualTo("USD");
        verify(accountService).createAccount(request);
    }

    @Test
    void createAccount_validatesNullRequest() {
        assertThatThrownBy(() -> accountMcpTools.createAccount(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null");
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_validatesNullAccountType() {
        var request = new CreateAccountRequest(100.0, null, "USD", 1L);

        assertThatThrownBy(() -> accountMcpTools.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account type");
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_validatesBlankCurrency() {
        var request = new CreateAccountRequest(100.0, AccountType.CURRENT_ACCOUNT, "  ", 1L);

        assertThatThrownBy(() -> accountMcpTools.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Currency");
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_validatesInvalidCurrencyFormat() {
        var request = new CreateAccountRequest(100.0, AccountType.CURRENT_ACCOUNT, "USDollar", 1L);

        assertThatThrownBy(() -> accountMcpTools.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("three-letter");
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_validatesNullCustomerId() {
        var request = new CreateAccountRequest(100.0, AccountType.CURRENT_ACCOUNT, "USD", null);

        assertThatThrownBy(() -> accountMcpTools.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_validatesNegativeCustomerId() {
        var request = new CreateAccountRequest(100.0, AccountType.CURRENT_ACCOUNT, "USD", -5L);

        assertThatThrownBy(() -> accountMcpTools.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_validatesNegativeBalance() {
        var request = new CreateAccountRequest(-10.0, AccountType.CURRENT_ACCOUNT, "USD", 1L);

        assertThatThrownBy(() -> accountMcpTools.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("balance");
        verifyNoInteractions(accountService);
    }

    @Test
    void getAccountById_delegatesToServiceAndReturnsAccountResponse() {
        when(accountService.getAccountById(VALID_ACCOUNT_ID)).thenReturn(sampleAccountResponse);

        AccountResponse result = accountMcpTools.getAccountById(VALID_ACCOUNT_ID);

        assertThat(result).isEqualTo(sampleAccountResponse);
        assertThat(result.id()).isEqualTo(VALID_ACCOUNT_ID);
        verify(accountService).getAccountById(VALID_ACCOUNT_ID);
    }

    @Test
    void getAccountById_validatesNullId() {
        assertThatThrownBy(() -> accountMcpTools.getAccountById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void getAccountById_validatesBlankId() {
        assertThatThrownBy(() -> accountMcpTools.getAccountById("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void listAccounts_delegatesToServiceAndReturnsListOfAccountResponses() {
        var accounts = List.of(sampleAccountResponse);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        List<AccountResponse> result = accountMcpTools.listAccounts();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(VALID_ACCOUNT_ID);
        verify(accountService).getAllAccounts();
    }

    @Test
    void creditAccount_delegatesToServiceAndReturnsTransactionResponse() {
        var request = new CreateTransactionRequest(500.0);
        when(accountService.credit(eq(VALID_ACCOUNT_ID), any(CreateTransactionRequest.class)))
                .thenReturn(sampleTransactionResponse);

        TransactionResponse result = accountMcpTools.creditAccount(VALID_ACCOUNT_ID, request);

        assertThat(result).isEqualTo(sampleTransactionResponse);
        assertThat(result.amount()).isEqualTo(100.0);
        assertThat(result.type()).isEqualTo(TransactionType.CREDIT);
        verify(accountService).credit(VALID_ACCOUNT_ID, request);
    }

    @Test
    void creditAccount_validatesNullAccountId() {
        var request = new CreateTransactionRequest(100.0);

        assertThatThrownBy(() -> accountMcpTools.creditAccount(null, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void creditAccount_validatesBlankAccountId() {
        var request = new CreateTransactionRequest(100.0);

        assertThatThrownBy(() -> accountMcpTools.creditAccount("  ", request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void creditAccount_validatesNullRequest() {
        assertThatThrownBy(() -> accountMcpTools.creditAccount(VALID_ACCOUNT_ID, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Transaction request");
        verifyNoInteractions(accountService);
    }

    @Test
    void creditAccount_validatesZeroAmount() {
        var request = new CreateTransactionRequest(0.0);

        assertThatThrownBy(() -> accountMcpTools.creditAccount(VALID_ACCOUNT_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount");
        verifyNoInteractions(accountService);
    }

    @Test
    void creditAccount_validatesNegativeAmount() {
        var request = new CreateTransactionRequest(-50.0);

        assertThatThrownBy(() -> accountMcpTools.creditAccount(VALID_ACCOUNT_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount");
        verifyNoInteractions(accountService);
    }

    @Test
    void debitAccount_delegatesToServiceAndReturnsTransactionResponse() {
        var request = new CreateTransactionRequest(200.0);
        var debitResponse = new TransactionResponse(2L, NOW, 200.0, TransactionType.DEBIT, VALID_ACCOUNT_ID);
        when(accountService.debit(eq(VALID_ACCOUNT_ID), any(CreateTransactionRequest.class)))
                .thenReturn(debitResponse);

        TransactionResponse result = accountMcpTools.debitAccount(VALID_ACCOUNT_ID, request);

        assertThat(result).isEqualTo(debitResponse);
        assertThat(result.type()).isEqualTo(TransactionType.DEBIT);
        verify(accountService).debit(VALID_ACCOUNT_ID, request);
    }

    @Test
    void debitAccount_propagatesInsufficientBalanceException() {
        var request = new CreateTransactionRequest(5000.0);
        when(accountService.debit(eq(VALID_ACCOUNT_ID), any(CreateTransactionRequest.class)))
                .thenThrow(new UnsufficientBalanceException("unsufficient balance"));

        assertThatThrownBy(() -> accountMcpTools.debitAccount(VALID_ACCOUNT_ID, request))
                .isInstanceOf(UnsufficientBalanceException.class)
                .hasMessageContaining("unsufficient");
        verify(accountService).debit(VALID_ACCOUNT_ID, request);
    }

    @Test
    void debitAccount_propagatesAccountNotFoundException() {
        var request = new CreateTransactionRequest(100.0);
        when(accountService.debit(eq("non-existent-id"), any(CreateTransactionRequest.class)))
                .thenThrow(new AccountNotFoundException("Account with id: non-existent-id not found"));

        assertThatThrownBy(() -> accountMcpTools.debitAccount("non-existent-id", request))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("not found");
        verify(accountService).debit("non-existent-id", request);
    }

    @Test
    void debitAccount_validatesNullAccountId() {
        var request = new CreateTransactionRequest(100.0);

        assertThatThrownBy(() -> accountMcpTools.debitAccount(null, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID");
        verifyNoInteractions(accountService);
    }

    @Test
    void debitAccount_validatesNullRequest() {
        assertThatThrownBy(() -> accountMcpTools.debitAccount(VALID_ACCOUNT_ID, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Transaction request");
        verifyNoInteractions(accountService);
    }

    @Test
    void debitAccount_validatesNegativeAmount() {
        var request = new CreateTransactionRequest(-10.0);

        assertThatThrownBy(() -> accountMcpTools.debitAccount(VALID_ACCOUNT_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount");
        verifyNoInteractions(accountService);
    }

    @Test
    void accountResponse_doesNotExposeJpaEntityFields() {
        assertThat(sampleAccountResponse).isInstanceOf(AccountResponse.class);
        assertThat(sampleAccountResponse.id()).isNotNull();
        assertThat(sampleAccountResponse.balance()).isNotNull();
        assertThat(sampleAccountResponse.type()).isNotNull();
        assertThat(sampleAccountResponse.status()).isNotNull();
        assertThat(sampleAccountResponse.currency()).isNotNull();
        assertThat(sampleAccountResponse.customerId()).isNotNull();
        assertThat(sampleAccountResponse.createdAt()).isNotNull();
        assertThat(sampleAccountResponse.updatedAt()).isNotNull();
    }

    @Test
    void transactionResponse_doesNotExposeAccountEntity() {
        assertThat(sampleTransactionResponse).isInstanceOf(TransactionResponse.class);
        assertThat(sampleTransactionResponse.id()).isNotNull();
        assertThat(sampleTransactionResponse.amount()).isNotNull();
        assertThat(sampleTransactionResponse.type()).isNotNull();
        assertThat(sampleTransactionResponse.accountId()).isEqualTo(VALID_ACCOUNT_ID);
        assertThat(sampleTransactionResponse.dateTime()).isNotNull();
    }
}
