package com.portfolio.banktransfercore.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.portfolio.banktransfercore.domain.shared.money.Money;
import com.portfolio.banktransfercore.domain.shared.money.SupportedCurrency;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTest {

  @Test
  @DisplayName(
      "Creates an account with accurately populated state when valid initial data is provided")
  void givenValidInitialData_whenCreatingAccount_thenStateIsAccuratelyPopulated() {
    // Given
    AccountId expectedId = new AccountId(UUID.randomUUID());
    AccountNumber expectedNumber = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("1000.00"), SupportedCurrency.USD);

    // When
    Account account = new Account(expectedId, expectedNumber, initialBalance);

    // Then
    assertThat(account)
        .returns(expectedId, Account::getId)
        .returns(expectedNumber, Account::getAccountNumber)
        .returns(initialBalance, Account::getBalance);
  }

  @Test
  @DisplayName("Decreases the balance accordingly when debiting an account with sufficient funds")
  void givenSufficientFunds_whenDebiting_thenBalanceDecreasesAccordingly() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("500.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money debitAmount = new Money(new BigDecimal("200.00"), SupportedCurrency.USD);
    Money expectedBalance = new Money(new BigDecimal("300.00"), SupportedCurrency.USD);

    // When
    account.debit(debitAmount);

    // Then
    assertThat(account.getBalance()).isEqualTo(expectedBalance);
  }

  @Test
  @DisplayName("Allows debiting the exact balance amount, resulting in a zero balance")
  void givenExactBalanceAmount_whenDebiting_thenBalanceBecomesZero() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money debitAmount = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Money expectedBalance = new Money(new BigDecimal("0.00"), SupportedCurrency.USD);

    // When
    account.debit(debitAmount);

    // Then
    assertThat(account.getBalance()).isEqualTo(expectedBalance);
  }

  @Test
  @DisplayName(
      "Throws an IllegalStateException when attempting to debit an account with insufficient funds")
  void givenInsufficientFunds_whenDebiting_thenThrowsIllegalStateException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money debitAmount = new Money(new BigDecimal("150.00"), SupportedCurrency.USD);

    // When / Then
    assertThatThrownBy(() -> account.debit(debitAmount))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Insufficient funds");
  }

  @Test
  @DisplayName("Throws an IllegalArgumentException when debiting a zero or negative amount")
  void givenZeroOrNegativeAmount_whenDebiting_thenThrowsIllegalArgumentException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money zeroAmount = new Money(new BigDecimal("0.00"), SupportedCurrency.USD);
    // Note: Money record constructor throws IllegalArgumentException if amount < 0,
    // so to test negative amount passing to debit, we would have to bypass Money constructor.
    // We'll test zero amount here.

    // When / Then
    assertThatThrownBy(() -> account.debit(zeroAmount))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Debit amount must be greater than zero");
  }

  @Test
  @DisplayName("Throws a CurrencyMismatchException when debiting a different currency")
  void givenDifferentCurrency_whenDebiting_thenThrowsCurrencyMismatchException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money penAmount = new Money(new BigDecimal("50.00"), SupportedCurrency.PEN);

    // When / Then
    assertThatThrownBy(() -> account.debit(penAmount))
        .isInstanceOf(
            com.portfolio.banktransfercore.domain.shared.money.CurrencyMismatchException.class)
        .hasMessageContaining("Cannot operate on different currencies");
  }

  @Test
  @DisplayName("Increases the balance accordingly when crediting an account with a valid amount")
  void givenValidAmount_whenCrediting_thenBalanceIncreasesAccordingly() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("150.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money creditAmount = new Money(new BigDecimal("50.00"), SupportedCurrency.USD);
    Money expectedBalance = new Money(new BigDecimal("200.00"), SupportedCurrency.USD);

    // When
    account.credit(creditAmount);

    // Then
    assertThat(account.getBalance()).isEqualTo(expectedBalance);
  }

  @Test
  @DisplayName("Throws an IllegalArgumentException when crediting a zero amount")
  void givenZeroAmount_whenCrediting_thenThrowsIllegalArgumentException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money zeroAmount = new Money(new BigDecimal("0.00"), SupportedCurrency.USD);

    // When / Then
    assertThatThrownBy(() -> account.credit(zeroAmount))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Credit amount must be greater than zero");
  }

  @Test
  @DisplayName("Throws a CurrencyMismatchException when crediting a different currency")
  void givenDifferentCurrency_whenCrediting_thenThrowsCurrencyMismatchException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    Money penAmount = new Money(new BigDecimal("50.00"), SupportedCurrency.PEN);

    // When / Then
    assertThatThrownBy(() -> account.credit(penAmount))
        .isInstanceOf(
            com.portfolio.banktransfercore.domain.shared.money.CurrencyMismatchException.class)
        .hasMessageContaining("Cannot operate on different currencies");
  }

  @Test
  @DisplayName("Throws a NullPointerException when creating an account with a null ID")
  void givenNullAccountId_whenCreatingAccount_thenThrowsNullPointerException() {
    // Given
    AccountNumber expectedNumber = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("1000.00"), SupportedCurrency.USD);

    // When / Then
    assertThatThrownBy(() -> new Account(null, expectedNumber, initialBalance))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Account ID cannot be null");
  }

  @Test
  @DisplayName("Throws a NullPointerException when creating an account with a null account number")
  void givenNullAccountNumber_whenCreatingAccount_thenThrowsNullPointerException() {
    // Given
    AccountId expectedId = new AccountId(UUID.randomUUID());
    Money initialBalance = new Money(new BigDecimal("1000.00"), SupportedCurrency.USD);

    // When / Then
    assertThatThrownBy(() -> new Account(expectedId, null, initialBalance))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Account number cannot be null");
  }

  @Test
  @DisplayName("Throws a NullPointerException when creating an account with a null balance")
  void givenNullBalance_whenCreatingAccount_thenThrowsNullPointerException() {
    // Given
    AccountId expectedId = new AccountId(UUID.randomUUID());
    AccountNumber expectedNumber = new AccountNumber("00219112345678901206");

    // When / Then
    assertThatThrownBy(() -> new Account(expectedId, expectedNumber, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Initial balance cannot be null");
  }

  @Test
  @DisplayName("Throws a NullPointerException when debiting a null amount")
  void givenNullAmount_whenDebiting_thenThrowsNullPointerException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    // When / Then
    assertThatThrownBy(() -> account.debit(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction amount cannot be null");
  }

  @Test
  @DisplayName("Throws a NullPointerException when crediting a null amount")
  void givenNullAmount_whenCrediting_thenThrowsNullPointerException() {
    // Given
    AccountId ANY_ID = new AccountId(UUID.randomUUID());
    AccountNumber ANY_NUMBER = new AccountNumber("00219112345678901206");
    Money initialBalance = new Money(new BigDecimal("100.00"), SupportedCurrency.USD);
    Account account = new Account(ANY_ID, ANY_NUMBER, initialBalance);

    // When / Then
    assertThatThrownBy(() -> account.credit(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction amount cannot be null");
  }
}
