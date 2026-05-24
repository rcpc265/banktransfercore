package com.portfolio.banktransfercore.domain.account;

import java.util.Objects;

public record AccountId(String value) {
    public AccountId {
        Objects.requireNonNull(value, "AccountId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("AccountId cannot be blank");
        }
    }
}
