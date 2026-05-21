package com.portfolio.banktransfercore.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class Account {
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^CTA-\\d{3,10}$");

    private final UUID id;
    private final String accountNumber;
    private BigDecimal balance;

    public Account(UUID id, String accountNumber, BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser null");

        if (accountNumber == null || !ACCOUNT_PATTERN.matcher(accountNumber).matches()) {
            throw new IllegalArgumentException("Número de cuenta inválido");
        }

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        }

        this.accountNumber = accountNumber;
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
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
}