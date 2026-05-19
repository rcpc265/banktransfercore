package com.portfolio.banktransfercore.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID id;
    private String accountNumber;
    private BigDecimal balance;

    public Account(UUID id, String accountNumber, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void debit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a cero");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Fondos insuficientes");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a cero");
        }
        this.balance = this.balance.add(amount);
    }

    public UUID getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
}