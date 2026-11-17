package com.ars.accountservice.response;
import com.ars.accountservice.domain.TransactionType;
import java.time.LocalDateTime;



public record TransactionResponse(
        Long id,
        LocalDateTime dateTime,
        double amount,
        TransactionType type,
        String accountId
)
{}
