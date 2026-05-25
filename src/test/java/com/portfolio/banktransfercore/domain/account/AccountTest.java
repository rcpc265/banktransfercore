package com.portfolio.banktransfercore.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class AccountTest {

    private static final String ANY_ACCOUNT_NUMBER = "00219112345678901206";

    @Nested
    @DisplayName("Creation and Validation Rules")
    class CreationAndValidationRules {

        @Test
        @DisplayName("Allows creating Account with valid details")
        void givenValidDetails_whenCreatingAccount_thenSucceeds() {
            // Given
            UUID id = UUID.randomUUID();
            BigDecimal initialBalance = new BigDecimal("500.00");

            // When
            Account account = new Account(id, ANY_ACCOUNT_NUMBER, initialBalance);

            // Then
            assertThat(account.getId()).isEqualTo(id);
            assertThat(account.getAccountNumber()).isEqualTo(ANY_ACCOUNT_NUMBER);
            assertThat(account.getBalance()).isEqualByComparingTo(initialBalance);
        }

        @Test
        @DisplayName("Allows creating Account with exactly zero balance")
        void givenZeroBalance_whenCreatingAccount_thenSucceeds() {
            // When & Then
            assertThatCode(() -> new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, BigDecimal.ZERO))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Does not allow creating Account with a null ID")
        void givenNullId_whenCreatingAccount_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");

            // When & Then
            assertThatThrownBy(() -> new Account(null, ANY_ACCOUNT_NUMBER, initialBalance))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Account ID cannot be null");
        }

        @Test
        @DisplayName("Does not allow creating Account with a null account number")
        void givenNullAccountNumber_whenCreatingAccount_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");

            // When & Then
            assertThatThrownBy(() -> new Account(UUID.randomUUID(), null, initialBalance))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Account number cannot be null");
        }

        @Test
        @DisplayName("Does not allow creating Account with a null balance")
        void givenNullBalance_whenCreatingAccount_thenThrowsException() {
            // When & Then
            assertThatThrownBy(() -> new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Initial balance cannot be negative");
        }

        @Test
        @DisplayName("Does not allow creating Account with a negative balance")
        void givenNegativeBalance_whenCreatingAccount_thenThrowsException() {
            // Given
            BigDecimal negativeBalance = new BigDecimal("-0.01");

            // When & Then
            assertThatThrownBy(() -> new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, negativeBalance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Initial balance cannot be negative");
        }
    }

    @Nested
    @DisplayName("Debit Operations")
    class DebitOperations {

        @Test
        @DisplayName("Decreases balance correctly when amount is valid")
        void givenValidAmount_whenDebiting_thenBalanceDecreases() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            BigDecimal debitAmount = new BigDecimal("30.00");
            BigDecimal expectedRemaining = new BigDecimal("70.00");

            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When
            account.debit(debitAmount);

            // Then
            assertThat(account.getBalance()).isEqualByComparingTo(expectedRemaining);
        }

        @Test
        @DisplayName("Allows clearing out balance to exactly zero")
        void givenExactBalanceAmount_whenDebiting_thenBalanceBecomesZero() {
            // Given
            BigDecimal exactBalance = new BigDecimal("50.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, exactBalance);

            // When
            account.debit(exactBalance);

            // Then
            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Does not allow debiting a null amount")
        void givenNullAmount_whenDebiting_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When & Then
            assertThatThrownBy(() -> account.debit(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("Does not allow debiting an amount of exactly zero")
        void givenZeroAmount_whenDebiting_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When & Then
            assertThatThrownBy(() -> account.debit(BigDecimal.ZERO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("Does not allow debiting a negative amount")
        void givenNegativeAmount_whenDebiting_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            BigDecimal negativeDebit = new BigDecimal("-10.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When & Then
            assertThatThrownBy(() -> account.debit(negativeDebit))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("Throws exception if debit amount exceeds balance")
        void givenExcessiveAmount_whenDebiting_thenThrowsException() {
            // Given
            BigDecimal lowBalance = new BigDecimal("50.00");
            BigDecimal excessiveDebit = new BigDecimal("50.01");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, lowBalance);

            // When & Then
            assertThatThrownBy(() -> account.debit(excessiveDebit))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Insufficient funds");
        }
    }

    @Nested
    @DisplayName("Credit Operations")
    class CreditOperations {

        @Test
        @DisplayName("Increases balance correctly when amount is valid")
        void givenValidAmount_whenCrediting_thenBalanceIncreases() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            BigDecimal creditAmount = new BigDecimal("50.00");
            BigDecimal expectedTotal = new BigDecimal("150.00");

            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When
            account.credit(creditAmount);

            // Then
            assertThat(account.getBalance()).isEqualByComparingTo(expectedTotal);
        }

        @Test
        @DisplayName("Does not allow crediting a null amount")
        void givenNullAmount_whenCrediting_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When & Then
            assertThatThrownBy(() -> account.credit(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("Does not allow crediting an amount of exactly zero")
        void givenZeroAmount_whenCrediting_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When & Then
            assertThatThrownBy(() -> account.credit(BigDecimal.ZERO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("Does not allow crediting a negative amount")
        void givenNegativeAmount_whenCrediting_thenThrowsException() {
            // Given
            BigDecimal initialBalance = new BigDecimal("100.00");
            BigDecimal negativeCredit = new BigDecimal("-10.00");
            Account account = new Account(UUID.randomUUID(), ANY_ACCOUNT_NUMBER, initialBalance);

            // When & Then
            assertThatThrownBy(() -> account.credit(negativeCredit))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must be greater than zero");
        }
    }
}