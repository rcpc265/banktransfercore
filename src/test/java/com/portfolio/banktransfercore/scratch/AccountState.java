package com.portfolio.banktransfercore.scratch;

public interface AccountState {

  default void validateDebit(Money amount) {
    throw new UnsupportedOperationException(
        "Developer error: You MUST override validateDebit() to allow withdrawals in this state");
  }

  default void validateCredit(Money amount) {
    throw new UnsupportedOperationException(
        "Developer error: You MUST override validateCredit() to allow deposits in this state");
  }
}
