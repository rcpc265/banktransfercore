package com.portfolio.banktransfercore.application.services;

import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import com.portfolio.banktransfercore.application.ports.out.AccountNotFoundException;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.application.ports.out.AccountsForFundsTransfer;
import com.portfolio.banktransfercore.domain.account.AccountNumber;
import com.portfolio.banktransfercore.domain.shared.money.Money;
import com.portfolio.banktransfercore.domain.shared.money.SupportedCurrency;
import java.math.BigDecimal;

public class FundsTransferService implements FundsTransferUseCase {

  private final AccountRepositoryPort accountRepositoryPort;

  public FundsTransferService(AccountRepositoryPort accountRepositoryPort) {
    this.accountRepositoryPort = accountRepositoryPort;
  }

  @Override
  public void execute(
      String sourceNumber, String destinationNumber, BigDecimal amount, String currency) {
    AccountNumber source = new AccountNumber(sourceNumber);
    AccountNumber dest = new AccountNumber(destinationNumber);

    AccountsForFundsTransfer accountsPair;
    try {
      accountsPair = accountRepositoryPort.loadAccountsForTransfer(source, dest);
    } catch (AccountNotFoundException e) {
      throw new IllegalArgumentException(
          e.getAccountNumber().equals(source)
              ? "Source account does not exist"
              : "Destination account does not exist");
    }

    var transferMoney = new Money(amount, SupportedCurrency.valueOf(currency));
    accountsPair.source().debit(transferMoney);
    accountsPair.destination().credit(transferMoney);

    accountRepositoryPort.save(accountsPair.source());
    accountRepositoryPort.save(accountsPair.destination());
  }
}
