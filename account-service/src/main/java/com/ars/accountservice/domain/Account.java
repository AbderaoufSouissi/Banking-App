package com.ars.accountservice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;
import static java.time.LocalDateTime.now;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = UUID)
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

    protected Account() {
    }

    public Account(String id, double balance, AccountType type, AccountStatus status, String currency, Long customerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.balance = balance;
        this.type = type;
        this.status = status;
        this.currency = currency;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    public static AccountBuilder builder() {
        return new AccountBuilder();
    }


    @PrePersist
    public void prePersist() {
        this.createdAt = now();
        this.updatedAt = now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = now();
    }


    public String getId() {
        return this.id;
    }

    public double getBalance() {
        return this.balance;
    }

    public AccountType getType() {
        return this.type;
    }

    public AccountStatus getStatus() {
        return this.status;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class AccountBuilder {
        private String id;
        private double balance;
        private AccountType type;
        private AccountStatus status;
        private String currency;
        private Long customerId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        AccountBuilder() {
        }

        public AccountBuilder id(String id) {
            this.id = id;
            return this;
        }

        public AccountBuilder balance(double balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder type(AccountType type) {
            this.type = type;
            return this;
        }

        public AccountBuilder status(AccountStatus status) {
            this.status = status;
            return this;
        }

        public AccountBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public AccountBuilder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public AccountBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AccountBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Account build() {
            return new Account(this.id, this.balance, this.type, this.status, this.currency, this.customerId, this.createdAt, this.updatedAt);
        }

        public String toString() {
            return "Account.AccountBuilder(id=" + this.id + ", balance=" + this.balance + ", type=" + this.type + ", status=" + this.status + ", currency=" + this.currency + ", customerId=" + this.customerId + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + ")";
        }
    }
}
