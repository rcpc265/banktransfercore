package com.portfolio.banktransfercore.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.portfolio.banktransfercore.domain.account.Account;
import com.portfolio.banktransfercore.domain.account.AccountId;
import com.portfolio.banktransfercore.domain.account.AccountNumber;
import com.portfolio.banktransfercore.domain.shared.money.Money;
import com.portfolio.banktransfercore.domain.shared.money.SupportedCurrency;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryAdapterTest {

  private static final String ANY_ACCOUNT_NUMBER = "12345678901234567890";
  private static final UUID ANY_ID = UUID.randomUUID();

  @Mock private AccountJpaRepository accountJpaRepository;

  private AccountRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new AccountRepositoryAdapter(accountJpaRepository);
  }

  @Test
  @DisplayName("Finds account by account number")
  void givenExistingAccountNumber_whenFindByAccountNumber_thenReturnsAccount() {
    // Given
    var entity =
        new AccountEntity(ANY_ID, ANY_ACCOUNT_NUMBER, BigDecimal.valueOf(500), "USD", "ACTIVE");
    given(accountJpaRepository.findByAccountNumber(ANY_ACCOUNT_NUMBER))
        .willReturn(Optional.of(entity));

    // When
    Optional<Account> result = adapter.findByAccountNumber(new AccountNumber(ANY_ACCOUNT_NUMBER));

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getId().value()).isEqualTo(ANY_ID);
    assertThat(result.get().getAccountNumber().value()).isEqualTo(ANY_ACCOUNT_NUMBER);
  }

  @Test
  @DisplayName("Returns empty when account number does not exist")
  void givenNonExistingAccountNumber_whenFindByAccountNumber_thenReturnsEmpty() {
    // Given
    given(accountJpaRepository.findByAccountNumber(ANY_ACCOUNT_NUMBER))
        .willReturn(Optional.empty());

    // When
    Optional<Account> result = adapter.findByAccountNumber(new AccountNumber(ANY_ACCOUNT_NUMBER));

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Finds account by id")
  void givenExistingId_whenFindById_thenReturnsAccount() {
    // Given
    var entity =
        new AccountEntity(ANY_ID, ANY_ACCOUNT_NUMBER, BigDecimal.valueOf(500), "USD", "ACTIVE");
    given(accountJpaRepository.findById(ANY_ID)).willReturn(Optional.of(entity));

    // When
    Optional<Account> result = adapter.findById(ANY_ID);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getId().value()).isEqualTo(ANY_ID);
  }

  @Test
  @DisplayName("Saves account to repository")
  void givenAccount_whenSave_thenPersistsEntity() {
    // Given
    var account =
        new Account(
            new AccountId(ANY_ID),
            new AccountNumber(ANY_ACCOUNT_NUMBER),
            Money.of("1000.00", SupportedCurrency.USD));

    // When
    adapter.save(account);

    // Then
    var expected =
        new AccountEntity(ANY_ID, ANY_ACCOUNT_NUMBER, BigDecimal.valueOf(1000.00), "USD", "ACTIVE");
    then(accountJpaRepository).should().save(refEq(expected));
  }
}
