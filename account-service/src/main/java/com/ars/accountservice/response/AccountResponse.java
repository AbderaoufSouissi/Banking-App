package com.ars.accountservice.response;

import com.ars.accountservice.domain.AccountStatus;
import com.ars.accountservice.domain.AccountType;
import java.time.LocalDateTime;



public record AccountResponse(
        String id,
        double balance,
        AccountType type,
        AccountStatus status,
        String currency,
        Long customerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{}
