package com.portfolio.banktransfercore.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.portfolio.banktransfercore.application.ports.out.AccountNotFoundException;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.application.ports.out.AccountsForFundsTransfer;
import com.portfolio.banktransfercore.domain.account.Account;
import com.portfolio.banktransfercore.domain.account.AccountId;
import com.portfolio.banktransfercore.domain.account.AccountNumber;
import com.portfolio.banktransfercore.domain.shared.money.Money;
import com.portfolio.banktransfercore.domain.shared.money.SupportedCurrency;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FundsTransferServiceTest {

  @Mock private AccountRepositoryPort accountRepositoryPort;

  @InjectMocks private FundsTransferService transferService;

  private static final String ANY_SOURCE_NUMBER = "00219112345678901206";
  private static final String ANY_DESTINATION_NUMBER = "98765432101234567819";

  @Test
  @DisplayName("Executes a successful transfer between two existing accounts")
  void givenValidAccountsAndFunds_whenExecutingTransfer_thenBalancesAreUpdatedAndSaved()
      throws AccountNotFoundException {
    // Given
    var anySourceId = UUID.randomUUID();
    var anyDestinationId = UUID.randomUUID();
    var initialSourceBalance = Money.of("500.00", SupportedCurrency.USD);
    var initialDestinationBalance = Money.of("100.00", SupportedCurrency.USD);
    var transferAmount = new BigDecimal("200.00");

    var sourceAccount =
        new Account(
            new AccountId(anySourceId), new AccountNumber(ANY_SOURCE_NUMBER), initialSourceBalance);
    var destinationAccount =
        new Account(
            new AccountId(anyDestinationId),
            new AccountNumber(ANY_DESTINATION_NUMBER),
            initialDestinationBalance);

    given(
            accountRepositoryPort.loadAccountsForTransfer(
                new AccountNumber(ANY_SOURCE_NUMBER), new AccountNumber(ANY_DESTINATION_NUMBER)))
        .willReturn(new AccountsForFundsTransfer(sourceAccount, destinationAccount));

    // When
    transferService.execute(ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, transferAmount, "USD");

    // Then
    var expectedSourceBalance = Money.of("300.00", SupportedCurrency.USD);
    var expectedDestinationBalance = Money.of("300.00", SupportedCurrency.USD);

    assertThat(sourceAccount.getBalance()).isEqualTo(expectedSourceBalance);
    assertThat(destinationAccount.getBalance()).isEqualTo(expectedDestinationBalance);

    then(accountRepositoryPort).should().save(sourceAccount);
    then(accountRepositoryPort).should().save(destinationAccount);
  }

  @Test
  @DisplayName("Throws exception when source account does not exist")
  void givenMissingSourceAccount_whenExecutingTransfer_thenThrowsException()
      throws AccountNotFoundException {
    // Given
    var anyAmount = new BigDecimal("100.00");

    given(
            accountRepositoryPort.loadAccountsForTransfer(
                new AccountNumber(ANY_SOURCE_NUMBER), new AccountNumber(ANY_DESTINATION_NUMBER)))
        .willThrow(new AccountNotFoundException(new AccountNumber(ANY_SOURCE_NUMBER)));

    // When & Then
    assertThatThrownBy(
            () ->
                transferService.execute(
                    ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, anyAmount, "USD"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Source account does not exist");

    then(accountRepositoryPort).should(never()).save(any(Account.class));
  }

  @Test
  @DisplayName("Throws exception when destination account does not exist")
  void givenMissingDestinationAccount_whenExecutingTransfer_thenThrowsException()
      throws AccountNotFoundException {
    // Given
    var anyAmount = new BigDecimal("100.00");

    given(
            accountRepositoryPort.loadAccountsForTransfer(
                new AccountNumber(ANY_SOURCE_NUMBER), new AccountNumber(ANY_DESTINATION_NUMBER)))
        .willThrow(new AccountNotFoundException(new AccountNumber(ANY_DESTINATION_NUMBER)));

    // When & Then
    assertThatThrownBy(
            () ->
                transferService.execute(
                    ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, anyAmount, "USD"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Destination account does not exist");

    then(accountRepositoryPort).should(never()).save(any(Account.class));
  }

  @Test
  @DisplayName("Throws exception when source account has insufficient funds")
  void
      givenSourceAccountWithInsufficientFunds_whenExecutingTransfer_thenThrowsExceptionAndSavesNothing()
          throws AccountNotFoundException {
    // Given
    var anySourceId = UUID.randomUUID();
    var anyDestinationId = UUID.randomUUID();
    var lowSourceBalance = Money.of("50.00", SupportedCurrency.USD);
    var excessiveTransferAmount = new BigDecimal("100.00");

    var sourceAccount =
        new Account(
            new AccountId(anySourceId), new AccountNumber(ANY_SOURCE_NUMBER), lowSourceBalance);
    var destinationAccount =
        new Account(
            new AccountId(anyDestinationId),
            new AccountNumber(ANY_DESTINATION_NUMBER),
            Money.of("200.00", SupportedCurrency.USD));

    given(
            accountRepositoryPort.loadAccountsForTransfer(
                new AccountNumber(ANY_SOURCE_NUMBER), new AccountNumber(ANY_DESTINATION_NUMBER)))
        .willReturn(new AccountsForFundsTransfer(sourceAccount, destinationAccount));

    // When & Then
    assertThatThrownBy(
            () ->
                transferService.execute(
                    ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, excessiveTransferAmount, "USD"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Insufficient funds");

    then(accountRepositoryPort).should(never()).save(any(Account.class));
  }
}
