package com.portfolio.banktransfercore.domain.shared.money;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Nested
    @DisplayName("Creation and Validation Rules")
    class CreationAndValidationRules {

        @Test
        @DisplayName("Does not allow creating Money with a null amount")
        void givenNullAmount_whenCreatingMoney_thenThrowsException() {
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(null, SupportedCurrency.PEN));
            assertEquals("Amount cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Does not allow creating Money with a null currency")
        void givenNullCurrency_whenCreatingMoney_thenThrowsException() {
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(new BigDecimal("100.00"), null));
            assertEquals("Currency cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Does not allow creating Money with a negative amount")
        void givenNegativeAmount_whenCreatingMoney_thenThrowsException() {
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> Money.of("-0.01", SupportedCurrency.PEN));
            assertEquals("Amount cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Allows creating Money with an amount of exactly zero")
        void givenZeroAmount_whenCreatingMoney_thenSucceeds() {
            assertDoesNotThrow(() -> Money.of("0.00", SupportedCurrency.PEN));
        }

        @Test
        @DisplayName("Normalizes scale to 2 decimals upon creation")
        void givenIntegerAmount_whenCreatingMoney_thenSetsScaleToTwo() {
            var money = Money.of("10", SupportedCurrency.PEN);
            assertEquals(new BigDecimal("10.00"), money.amount());
        }

        @Test
        @DisplayName("Normalizes scale to 2 decimals when using the zero factory method")
        void givenZeroFactoryMethod_whenCalled_thenCreatesZeroAmountWithTwoDecimals() {
            var money = Money.zero(SupportedCurrency.PEN);
            assertEquals(new BigDecimal("0.00"), money.amount());
        }

        @Test
        @DisplayName("Applies Banker's Rounding (HALF_EVEN) to the nearest even number")
        void givenHalfCent_whenCreatingMoney_thenRoundsToNearestEven() {
            var roundDown = Money.of("10.025", SupportedCurrency.PEN);
            var roundUp = Money.of("10.035", SupportedCurrency.PEN);

            assertAll(
                    () -> assertEquals(new BigDecimal("10.02"), roundDown.amount()),
                    () -> assertEquals(new BigDecimal("10.04"), roundUp.amount())
            );
        }
    }

    @Nested
    @DisplayName("Equality and Identity (Value Object Rules)")
    class Equality {

        @Test
        @DisplayName("Two Money objects with same amount and currency are equal")
        void givenSameAmountAndCurrency_whenCompared_thenAreEquals() {
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("100.00", SupportedCurrency.PEN);

            assertEquals(moneyA, moneyB);
        }

        @Test
        @DisplayName("Equal Money objects must produce the exact same hash code")
        void givenSameAmountAndCurrency_whenCheckingHashCode_thenHashCodesMatch() {
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("100.00", SupportedCurrency.PEN);

            assertEquals(moneyA.hashCode(), moneyB.hashCode());
        }

        @Test
        @DisplayName("Two Money objects with different string scales but same value are equal")
        void givenDifferentScaleStrings_whenCompared_thenAreEquals() {
            var moneyA = Money.of("10", SupportedCurrency.PEN);
            var moneyB = Money.of("10.00", SupportedCurrency.PEN);

            assertEquals(moneyA, moneyB);
        }

        @Test
        @DisplayName("Two Money objects with same amount but different currency are not equal")
        void givenSameAmountDifferentCurrency_whenCompared_thenAreNotEquals() {
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("100.00", SupportedCurrency.USD);

            assertNotEquals(moneyA, moneyB);
        }

        @Test
        @DisplayName("Two Money objects with different amounts are not equal")
        void givenDifferentAmounts_whenCompared_thenAreNotEquals() {
            var moneyA = Money.of("100.00", SupportedCurrency.PEN);
            var moneyB = Money.of("50.00", SupportedCurrency.PEN);

            assertNotEquals(moneyA, moneyB);
        }
    }

    @Nested
    @DisplayName("Immutability and Mathematical Operations")
    class ImmutabilityAndMath {

        @Test
        @DisplayName("Addition returns a new Money object with the sum without mutating original")
        void givenSameCurrency_whenAdded_thenReturnsNewMoneyWithSum() {
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToAdd = Money.of("50.00", SupportedCurrency.PEN);

            var result = baseAmount.add(amountToAdd);

            assertAll(
                    () -> assertEquals(new BigDecimal("150.00"), result.amount()),
                    () -> assertEquals(SupportedCurrency.PEN, result.currency()),
                    () -> assertEquals(new BigDecimal("100.00"), baseAmount.amount())
            );
        }

        @Test
        @DisplayName("Addition throws exception if currencies do not match")
        void givenDifferentCurrencies_whenAdded_thenThrowsException() {
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToAdd = Money.of("50.00", SupportedCurrency.USD);

            var exception = assertThrows(CurrencyMismatchException.class,
                    () -> baseAmount.add(amountToAdd));
            assertTrue(exception.getMessage().contains("Cannot operate on different currencies"));
        }

        @Test
        @DisplayName("Subtraction returns a new Money object with the difference without mutating original")
        void givenSameCurrency_whenSubtracting_thenReturnsNewMoney() {
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("30.00", SupportedCurrency.PEN);

            var result = baseAmount.subtract(amountToSubtract);

            assertAll(
                    () -> assertEquals(new BigDecimal("70.00"), result.amount()),
                    () -> assertEquals(new BigDecimal("100.00"), baseAmount.amount())
            );
        }

        @Test
        @DisplayName("Subtraction resulting in exactly zero is allowed")
        void givenSameAmount_whenSubtracting_thenResultIsZero() {
            var baseAmount = Money.of("50.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("50.00", SupportedCurrency.PEN);

            var result = baseAmount.subtract(amountToSubtract);

            assertEquals(new BigDecimal("0.00"), result.amount());
        }

        @Test
        @DisplayName("Subtraction throws exception if currencies do not match")
        void givenDifferentCurrencies_whenSubtracting_thenThrowsException() {
            var baseAmount = Money.of("100.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("50.00", SupportedCurrency.USD);

            var exception = assertThrows(CurrencyMismatchException.class,
                    () -> baseAmount.subtract(amountToSubtract));
            assertTrue(exception.getMessage().contains("Cannot operate on different currencies"));
        }

        @Test
        @DisplayName("Subtraction throws exception if result would be negative")
        void givenLargerAmount_whenSubtracting_thenThrowsException() {
            var baseAmount = Money.of("30.00", SupportedCurrency.PEN);
            var amountToSubtract = Money.of("50.00", SupportedCurrency.PEN);

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> baseAmount.subtract(amountToSubtract));

            assertEquals("Amount cannot be negative", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Comparisons")
    class Comparisons {

        @Test
        @DisplayName("isGreaterThan returns true only if strictly greater")
        void givenAmounts_whenIsGreaterThan_thenEvaluatesCorrectly() {
            var fifty = Money.of("50.00", SupportedCurrency.PEN);
            var thirty = Money.of("30.00", SupportedCurrency.PEN);
            var anotherFifty = Money.of("50.00", SupportedCurrency.PEN);

            assertAll(
                    () -> assertTrue(fifty.isGreaterThan(thirty), "50 should be greater than 30"),
                    () -> assertFalse(thirty.isGreaterThan(fifty), "30 should not be greater than 50"),
                    () -> assertFalse(fifty.isGreaterThan(anotherFifty), "50 should not be greater than 50 (strict inequality)")
            );
        }

        @Test
        @DisplayName("isGreaterThan throws exception if currencies do not match")
        void givenDifferentCurrencies_whenIsGreaterThan_thenThrowsException() {
            var pen = Money.of("50.00", SupportedCurrency.PEN);
            var usd = Money.of("30.00", SupportedCurrency.USD);

            var exception = assertThrows(CurrencyMismatchException.class,
                    () -> pen.isGreaterThan(usd));
            assertTrue(exception.getMessage().contains("Cannot operate on different currencies"));
        }
    }
}