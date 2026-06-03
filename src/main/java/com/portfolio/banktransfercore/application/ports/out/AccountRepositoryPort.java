package com.portfolio.banktransfercore.application.ports.out;

import com.portfolio.banktransfercore.domain.account.Account;
import com.portfolio.banktransfercore.domain.account.AccountNumber;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
  Optional<Account> findById(UUID id);

  Optional<Account> findByAccountNumber(AccountNumber accountNumber);

  AccountsForFundsTransfer loadAccountsForTransfer(
      AccountNumber sourceAccountNumber, AccountNumber destinationAccountNumber)
      throws AccountNotFoundException;

  void save(Account account);
}
