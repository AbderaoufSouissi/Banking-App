package com.ars.accountservice.request;

import com.ars.accountservice.domain.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateAccountRequest(
        @DecimalMin(value = "0.0", message = "balance must be greater than or equal to zero")
        double balance,
        @NotNull(message = "accountType is required")
        AccountType accountType,
        @NotBlank(message = "currency is required")
        @Pattern(regexp = "[A-Z]{3}", message = "currency must be a three-letter ISO 4217 code")
        String currency,
        @NotNull(message = "customerId is required")
        @Positive(message = "customerId must be greater than zero")
        Long customerId
) {
}
