package com.portfolio.banktransfercore.domain.account;

import com.portfolio.banktransfercore.domain.shared.money.Money;
import java.util.Objects;

public class Account {
  private final AccountId id;
  private final AccountNumber accountNumber;
  private Money balance;
  private AccountStatus status = AccountStatus.ACTIVE;

  public Account(AccountId id, AccountNumber accountNumber, Money balance) {
    this(id, accountNumber, balance, AccountStatus.ACTIVE);
  }

  private Account(AccountId id, AccountNumber accountNumber, Money balance, AccountStatus status) {
    this.id = Objects.requireNonNull(id, "Account ID cannot be null");
    this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
    this.balance = Objects.requireNonNull(balance, "Initial balance cannot be null");
    this.status = Objects.requireNonNull(status, "Status cannot be null");
  }

  public static Account reconstitute(
      AccountId id, AccountNumber accountNumber, Money balance, AccountStatus status) {
    return new Account(id, accountNumber, balance, status);
  }

  public void debit(Money amount) {
    this.status.validateDebit(amount);
    requireValidTransactionAmount(amount, "Debit amount must be greater than zero");

    if (this.balance.isLessThan(amount)) {
      throw new IllegalStateException("Insufficient funds");
    }

    this.balance = this.balance.subtract(amount);
  }

  public void credit(Money amount) {
    this.status.validateCredit(amount);
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

  public AccountStatus getStatus() {
    return status;
  }

  // --- State Transitions (delegated to AccountStatus) ---

  public void investigate() {
    this.status = this.status.onInvestigate();
  }

  public void freeze() {
    this.status = this.status.onFreeze();
  }

  public void close() {
    this.status = this.status.onClose();
  }

  public void reactivate() {
    this.status = this.status.onReactivate();
  }

  public void markDormant() {
    this.status = this.status.onMarkDormant();
  }
}
