package com.portfolio.banktransfercore.scratch;

public class Money {
  private final int amount;

  public Money(int amount) {
    this.amount = amount;
  }

  public boolean isGreaterThan(Money other) {
    return this.amount > other.amount;
  }
}
