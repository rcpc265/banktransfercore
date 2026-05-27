package com.portfolio.banktransfercore.domain.account;

import com.portfolio.banktransfercore.domain.shared.money.Money;
import java.util.Objects;

public class Account {
  private final AccountId id;
  private final AccountNumber accountNumber;
  private Money balance;

  public Account(AccountId id, AccountNumber accountNumber, Money balance) {
    this.id = Objects.requireNonNull(id, "Account ID cannot be null");
    this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
    this.balance = Objects.requireNonNull(balance, "Initial balance cannot be null");
  }

  public void debit(Money amount) {
    requireValidTransactionAmount(amount, "Debit amount must be greater than zero");

    if (this.balance.isLessThan(amount)) {
      throw new IllegalStateException("Insufficient funds");
    }

    this.balance = this.balance.subtract(amount);
  }

  public void credit(Money amount) {
    requireValidTransactionAmount(amount, "Credit amount must be greater than zero");

    this.balance = this.balance.add(amount);
  }

  private void requireValidTransactionAmount(Money amount, String errorMessage) {
    Objects.requireNonNull(amount, "Transaction amount cannot be null");

    if (!amount.isPositive()) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public AccountId getId() {
    return id;
  }

  public AccountNumber getAccountNumber() {
    return accountNumber;
  }

  public Money getBalance() {
    return balance;
  }
}
