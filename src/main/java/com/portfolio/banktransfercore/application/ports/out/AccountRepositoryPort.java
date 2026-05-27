package com.portfolio.banktransfercore.application.ports.out;

import com.portfolio.banktransfercore.domain.account.Account;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
  Optional<Account> findById(UUID id);

  Optional<Account> findByAccountNumber(String accountNumber);

  void save(Account account);
}
