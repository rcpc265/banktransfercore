package com.portfolio.banktransfercore.domain.shared.money;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {

    @Nested
    @DisplayName("Creation and Validation Rules")
    class CreationAndValidationRules {

        @Test
        @DisplayName("Does not allow creating Money with a null amount")
        void givenNullAmount_whenCreatingMoney_thenThrowsException() {
            // When & Then
            assertThatThrownBy(() -> new Money(null, SupportedCurrency.PEN))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Amount cannot be null");
        }

        @Test
        @DisplayName("Does not allow creating Money with a null currency")
        void givenNullCurrency_whenCreatingMoney_thenThrowsException() {
            // When & Then
            assertThatThrownBy(() -> new Money(new BigDecimal("100.00"), null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Currency cannot be null");
        }

        @Test
        @DisplayName("Does not allow creating Money with a negative amount")
        void givenNegativeAmount_whenCreatingMoney_thenThrowsException() {
            // When & Then
            assertThatThrownBy(() -> Money.of("-0.01", SupportedCurrency.PEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount cannot be negative");
        }

        @Test
        @DisplayName("Allows creating Money with an amount of exactly zero")
        void givenZeroAmount_whenCreatingMoney_thenSucceeds() {
            // When & Then
            assertThatCode(() -> Money.of("0.00", SupportedCurrency.PEN))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Normalizes scale to 2 decimals upon creation")
        void givenIntegerAmount_whenCreatingMoney_thenSetsScaleToTwo() {
            // When
            var money = Money.of("10", SupportedCurrency.PEN);

            // Then
            assertThat(money.amount()).isEqualTo(new BigDecimal("10.00"));
        }

        @Test
        @DisplayName("Normalizes scale to 2 decimals when using the zero factory method")
        void givenZeroFactoryMethod_whenCalled_thenCreatesZeroAmountWithTwoDecimals() {
            // When
            var money = Money.zero(SupportedCurrency.PEN);

            // Then
            assertThat(money.amount()).isEqualTo(new BigDecimal("0.00"));
        }

        @Test
        @DisplayName("Applies Banker's Rounding (HALF_EVEN) to the nearest even number")
        void givenHalfCent_whenCreatingMoney_thenRoundsToNearestEven() {
            // Given
            var expectedRoundDown = new BigDecimal("10.02");
            var expectedRoundUp = new BigDecimal("10.04");

            // When
            var roundDown = Money.of("10.025", SupportedCurrency.PEN);
            var roundUp = Money.of("10.035", SupportedCurrency.PEN);

            // Then
            assertThat(roundDown.amount()).isEqualTo(expectedRoundDown);
            assertThat(roundUp.amount()).isEqualTo(expectedRoundUp);
        }
    }

    @Nested
    @DisplayName("Equality and Identity (Value Object Rules)")
    class Equality {

        @Test
        @DisplayName("Two Money objects with same amount and currency are equal")
        void givenSameAmountAndCurrency_whenCompared_thenAreEquals() {
            // Given
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("100.00", SupportedCurrency.PEN);

            // When & Then
            assertThat(moneyA).isEqualTo(moneyB);
        }

        @Test
        @DisplayName("Equal Money objects must produce the exact same hash code")
        void givenSameAmountAndCurrency_whenCheckingHashCode_thenHashCodesMatch() {
            // Given
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("100.00", SupportedCurrency.PEN);

            // When & Then
            assertThat(moneyA.hashCode()).isEqualTo(moneyB.hashCode());
        }

        @Test
        @DisplayName("Two Money objects with different string scales but same value are equal")
        void givenDifferentScaleStrings_whenCompared_thenAreEquals() {
            // Given
            var moneyA = Money.of("10", SupportedCurrency.PEN);
            var moneyB = Money.of("10.00", SupportedCurrency.PEN);

            // When & Then
            assertThat(moneyA).isEqualTo(moneyB);
        }

        @Test
        @DisplayName("Two Money objects with same amount but different currency are not equal")
        void givenSameAmountDifferentCurrency_whenCompared_thenAreNotEquals() {
            // Given
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("100.00", SupportedCurrency.USD);

            // When & Then
            assertThat(moneyA).isNotEqualTo(moneyB);
        }

        @Test
        @DisplayName("Two Money objects with different amounts are not equal")
        void givenDifferentAmounts_whenCompared_thenAreNotEquals() {
            // Given
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("50.00", SupportedCurrency.PEN);

            // When & Then
            assertThat(moneyA).isNotEqualTo(moneyB);
        }
    }

    @Nested
    @DisplayName("Immutability and Mathematical Operations")
    class ImmutabilityAndMath {

        @Test
        @DisplayName("Addition returns a new Money object with the sum without mutating original")
        void givenSameCurrency_whenAdded_thenReturnsNewMoneyWithSum() {
            // Given
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToAdd = Money.of("50.00", SupportedCurrency.PEN);

            // When
            var result = baseAmount.add(amountToAdd);

            // Then
            assertThat(result.amount()).isEqualTo(new BigDecimal("150.00"));
            assertThat(result.currency()).isEqualTo(SupportedCurrency.PEN);
            assertThat(baseAmount.amount()).isEqualTo(new BigDecimal("100.00")); // Original unchanged
        }

        @Test
        @DisplayName("Addition throws exception if currencies do not match")
        void givenDifferentCurrencies_whenAdded_thenThrowsException() {
            // Given
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToAdd = Money.of("50.00", SupportedCurrency.USD);

            // When & Then
            assertThatThrownBy(() -> baseAmount.add(amountToAdd))
                    .isInstanceOf(CurrencyMismatchException.class)
                    .hasMessageContaining("Cannot operate on different currencies");
        }

        @Test
        @DisplayName("Subtraction returns a new Money object with the difference without mutating original")
        void givenSameCurrency_whenSubtracting_thenReturnsNewMoney() {
            // Given
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("30.00", SupportedCurrency.PEN);

            // When
            var result = baseAmount.subtract(amountToSubtract);

            // Then
            assertThat(result.amount()).isEqualTo(new BigDecimal("70.00"));
            assertThat(baseAmount.amount()).isEqualTo(new BigDecimal("100.00")); // Original unchanged
        }

        @Test
        @DisplayName("Subtraction resulting in exactly zero is allowed")
        void givenSameAmount_whenSubtracting_thenResultIsZero() {
            // Given
            var baseAmount = Money.of("50.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("50.00", SupportedCurrency.PEN);

            // When
            var result = baseAmount.subtract(amountToSubtract);

            // Then
            assertThat(result.amount()).isEqualTo(new BigDecimal("0.00"));
        }

        @Test
        @DisplayName("Subtraction throws exception if currencies do not match")
        void givenDifferentCurrencies_whenSubtracting_thenThrowsException() {
            // Given
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("50.00", SupportedCurrency.USD);

            // When & Then
            assertThatThrownBy(() -> baseAmount.subtract(amountToSubtract))
                    .isInstanceOf(CurrencyMismatchException.class)
                    .hasMessageContaining("Cannot operate on different currencies");
        }

        @Test
        @DisplayName("Subtraction throws exception if result would be negative")
        void givenLargerAmount_whenSubtracting_thenThrowsException() {
            // Given
            var baseAmount = Money.of("30.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("50.00", SupportedCurrency.PEN);

            // When & Then
            assertThatThrownBy(() -> baseAmount.subtract(amountToSubtract))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount cannot be negative");
        }
    }

    @Nested
    @DisplayName("Comparisons")
    class Comparisons {

        @Test
        @DisplayName("isGreaterThan returns true only if strictly greater")
        void givenAmounts_whenIsGreaterThan_thenEvaluatesCorrectly() {
            // Given
            var fifty = Money.of("50.00", SupportedCurrency.PEN);
            var thirty = Money.of("30.00", SupportedCurrency.PEN);
            var anotherFifty = Money.of("50.00", SupportedCurrency.PEN);

            // When & Then
            assertThat(fifty.isGreaterThan(thirty)).isTrue();
            assertThat(thirty.isGreaterThan(fifty)).isFalse();
            assertThat(fifty.isGreaterThan(anotherFifty)).isFalse();
        }

        @Test
        @DisplayName("isGreaterThan throws exception if currencies do not match")
        void givenDifferentCurrencies_whenIsGreaterThan_thenThrowsException() {
            // Given
            var pen = Money.of("50.00", SupportedCurrency.PEN);
            var usd = Money.of("30.00", SupportedCurrency.USD);

            // When & Then
            assertThatThrownBy(() -> pen.isGreaterThan(usd))
                    .isInstanceOf(CurrencyMismatchException.class)
                    .hasMessageContaining("Cannot operate on different currencies");
        }
    }
}