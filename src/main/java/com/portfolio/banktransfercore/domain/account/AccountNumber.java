package com.portfolio.banktransfercore.domain.account;

import java.util.Objects;

public record AccountNumber(String value) implements Comparable<AccountNumber> {

  @Override
  public int compareTo(AccountNumber other) {
    return this.value.compareTo(other.value);
  }

  public AccountNumber {
    Objects.requireNonNull(value, "Account number cannot be null");

    if (value.isBlank()) {
      throw new IllegalArgumentException("Account number cannot be blank");
    }

    if (value.length() != 20) {
      throw new IllegalArgumentException("Account number must be exactly 20 digits");
    }

    if (!value.matches("^[0-9]+$")) {
      throw new IllegalArgumentException("Account number must contain only digits");
    }
  }
}
