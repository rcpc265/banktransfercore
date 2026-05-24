package com.portfolio.banktransfercore.domain.shared.money;

import java.util.Currency;

public enum SupportedCurrency {
    PEN("PEN"),
    USD("USD");

    private final Currency javaCurrency;

    SupportedCurrency(String code) {
        this.javaCurrency = Currency.getInstance(code);
    }

    public Currency unwrap() {
        return javaCurrency;
    }
}