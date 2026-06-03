package com.portfolio.banktransfercore.adapter.out.persistence;

import com.portfolio.banktransfercore.application.ports.out.AccountNotFoundException;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.application.ports.out.AccountsForFundsTransfer;
import com.portfolio.banktransfercore.domain.account.Account;
import com.portfolio.banktransfercore.domain.account.AccountId;
import com.portfolio.banktransfercore.domain.account.AccountNumber;
import com.portfolio.banktransfercore.domain.account.AccountStatus;
import com.portfolio.banktransfercore.domain.shared.money.Money;
import com.portfolio.banktransfercore.domain.shared.money.SupportedCurrency;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {

  private final AccountJpaRepository accountJpaRepository;

  public AccountRepositoryAdapter(AccountJpaRepository accountJpaRepository) {
    this.accountJpaRepository = accountJpaRepository;
  }

  @Override
  public Optional<Account> findById(UUID id) {
    return accountJpaRepository.findById(id).map(this::mapToDomain);
  }

  @Override
  public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
    return accountJpaRepository.findByAccountNumber(accountNumber.value()).map(this::mapToDomain);
  }

  @Override
  public AccountsForFundsTransfer loadAccountsForTransfer(
      AccountNumber sourceAccountNumber, AccountNumber destinationAccountNumber)
      throws AccountNotFoundException {

    AccountNumber firstToLock = sourceAccountNumber;
    AccountNumber secondToLock = destinationAccountNumber;
    boolean isSourceFirst = true;

    if (sourceAccountNumber.compareTo(destinationAccountNumber) > 0) {
      firstToLock = destinationAccountNumber;
      secondToLock = sourceAccountNumber;
      isSourceFirst = false;
    }

    Account firstAccount = fetchAndMapLockedAccount(firstToLock);
    Account secondAccount = fetchAndMapLockedAccount(secondToLock);

    if (isSourceFirst) {
      return new AccountsForFundsTransfer(firstAccount, secondAccount);
    } else {
      return new AccountsForFundsTransfer(secondAccount, firstAccount);
    }
  }

  @Override
  public void save(Account account) {
    AccountEntity entity = mapToEntity(account);
    accountJpaRepository.save(entity);
  }

  private Account mapToDomain(AccountEntity entity) {
    return Account.reconstitute(
        new AccountId(entity.getId()),
        new AccountNumber(entity.getAccountNumber()),
        new Money(entity.getBalance(), SupportedCurrency.valueOf(entity.getCurrency())),
        AccountStatus.valueOf(entity.getStatus()));
  }

  private AccountEntity mapToEntity(Account account) {
    return new AccountEntity(
        account.getId().value(),
        account.getAccountNumber().value(),
        account.getBalance().amount(),
        account.getBalance().currency().name(),
        account.getStatus().name());
  }

  private Account fetchAndMapLockedAccount(AccountNumber accountNumber)
      throws AccountNotFoundException {
    AccountEntity entity =
        accountJpaRepository
            .findLockedByAccountNumber(accountNumber.value())
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    return mapToDomain(entity);
  }
}
