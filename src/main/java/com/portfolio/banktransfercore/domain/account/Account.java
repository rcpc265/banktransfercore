package com.portfolio.banktransfercore.domain.account;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final String accountNumber;
    private BigDecimal balance;

    public Account(UUID id, String accountNumber, BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "Account ID cannot be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.balance = balance;
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.balance = this.balance.add(amount);
    }

    public UUID getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
}

