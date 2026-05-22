package com.portfolio.banktransfercore.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final AccountNumber accountNumber;
    private BigDecimal balance;

    public Account(UUID id, AccountNumber accountNumber, BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "El número de cuenta no puede ser null");

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        }
        this.balance = balance;
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a cero");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Fondos insuficientes");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a cero");
        }
        this.balance = this.balance.add(amount);
    }

    public UUID getId() { return id; }
    public AccountNumber getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
}