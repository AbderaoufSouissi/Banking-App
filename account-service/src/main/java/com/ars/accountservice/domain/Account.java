package com.ars.accountservice.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;
import static java.time.LocalDateTime.now;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "accounts")
public class Account {
    @Id @GeneratedValue(strategy = UUID)
    private String id;
    private double balance;
    @Enumerated(STRING)
    private AccountType type;
    @Enumerated(STRING)
    private AccountStatus status;
    private String currency;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = now();
        this.updatedAt = now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = now();
    }


}
