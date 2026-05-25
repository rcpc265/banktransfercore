package com.portfolio.banktransfercore.domain.shared.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, SupportedCurrency currency) {
    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Money of(String amount, SupportedCurrency currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    public static Money zero(SupportedCurrency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        ensureSameCurrencyWith(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        ensureSameCurrencyWith(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        ensureSameCurrencyWith(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        ensureSameCurrencyWith(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void ensureSameCurrencyWith(Money other) {
        if (this.currency != other.currency) {
            throw new CurrencyMismatchException(
                    "Cannot operate on different currencies: " + this.currency + " and " + other.currency
            );
        }
    }
}