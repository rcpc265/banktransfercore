package com.portfolio.banktransfercore.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberTest {

    @Nested
    @DisplayName("Creation and Validation Rules")
    class CreationAndValidationRules {

        @Test
        @DisplayName("Does not allow creating AccountNumber with a null value")
        void givenNullValue_whenCreatingAccountNumber_thenThrowsException() {
            var exception = assertThrows(NullPointerException.class,
                    () -> new AccountNumber(null));
            assertEquals("Account number cannot be null", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Does not allow creating AccountNumber with a blank value")
        void givenBlankValue_whenCreatingAccountNumber_thenThrowsException(String blankValue) {
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new AccountNumber(blankValue));
            assertEquals("Account number cannot be blank", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"123", "002191123456789012345"})
        @DisplayName("Does not allow creating AccountNumber with invalid length (not 20 digits)")
        void givenInvalidLength_whenCreatingAccountNumber_thenThrowsException(String invalidLength) {
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new AccountNumber(invalidLength));
            assertEquals("Account number must be exactly 20 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Does not allow creating AccountNumber with non-numeric characters")
        void givenNonNumericValue_whenCreatingAccountNumber_thenThrowsException() {
            String nonNumeric = "002191123456789012AA";
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new AccountNumber(nonNumeric));
            assertEquals("Account number must contain only digits", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Equality and Identity (Value Object Rules)")
    class Equality {

        @Test
        @DisplayName("Two AccountNumber objects with the same value are equal")
        void givenSameValue_whenCompared_thenAreEquals() {
            var accountA = new AccountNumber("00219112345678901206");
            var accountB = new AccountNumber("00219112345678901206");

            assertEquals(accountA, accountB);
        }

        @Test
        @DisplayName("Two AccountNumber objects with the same value have identical hash codes")
        void givenSameValue_whenCheckingHashCode_thenHashCodesMatch() {
            var accountA = new AccountNumber("00219112345678901206");
            var accountB = new AccountNumber("00219112345678901206");

            assertEquals(accountA.hashCode(), accountB.hashCode());
        }

        @Test
        @DisplayName("Two AccountNumber objects with different values are not equal")
        void givenDifferentValues_whenCompared_thenAreNotEquals() {
            var accountA = new AccountNumber("00219112345678901206");
            var accountB = new AccountNumber("98765432101234567819");

            assertNotEquals(accountA, accountB);
        }
    }
}