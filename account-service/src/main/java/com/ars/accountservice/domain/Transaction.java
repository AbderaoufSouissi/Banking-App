package com.ars.accountservice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    private double amount;
    private TransactionType type;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


    protected Transaction() {
    }

    public Transaction(Long id, LocalDateTime dateTime, double amount, TransactionType type, Account account) {
        this.id = id;
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.account = account;
    }



    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public double getAmount() {
        return this.amount;
    }

    public TransactionType getType() {
        return this.type;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static class TransactionBuilder {
        private Long id;
        private LocalDateTime dateTime;
        private double amount;
        private TransactionType type;
        private Account account;

        TransactionBuilder() {
        }

        public TransactionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public TransactionBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public TransactionBuilder account(Account account) {
            this.account = account;
            return this;
        }

        public Transaction build() {
            return new Transaction(this.id, this.dateTime, this.amount, this.type, this.account);
        }

        public String toString() {
            return "Transaction.TransactionBuilder(id=" + this.id + ", dateTime=" + this.dateTime + ", amount=" + this.amount + ", type=" + this.type + ", account=" + this.account + ")";
        }
    }
}
