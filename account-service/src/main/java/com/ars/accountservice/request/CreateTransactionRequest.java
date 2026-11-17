package com.ars.accountservice.request;

import jakarta.validation.constraints.Positive;

public record CreateTransactionRequest(
        @Positive(message = "amount must be greater than zero")
        double amount
) {
}
