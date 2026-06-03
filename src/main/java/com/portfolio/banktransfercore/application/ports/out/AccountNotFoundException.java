package com.portfolio.banktransfercore.application.ports.out;

import com.portfolio.banktransfercore.domain.account.AccountNumber;

public class AccountNotFoundException extends Exception {

  private final transient AccountNumber accountNumber;

  public AccountNotFoundException(AccountNumber accountNumber) {
    super("Account " + accountNumber.value() + " not found");
    this.accountNumber = accountNumber;
  }

  public AccountNumber getAccountNumber() {
    return accountNumber;
  }
}
