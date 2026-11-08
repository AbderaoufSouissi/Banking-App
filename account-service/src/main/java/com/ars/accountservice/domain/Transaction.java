package com.ars.accountservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    private double amount;
    private TransactionType type;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
