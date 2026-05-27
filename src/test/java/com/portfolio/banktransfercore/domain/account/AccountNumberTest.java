package com.portfolio.banktransfercore.domain.account;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AccountNumberTest {

  @Nested
  @DisplayName("Creation and Validation Rules")
  class CreationAndValidationRules {

    @Test
    @DisplayName("Does not allow creating AccountNumber with a null value")
    void givenNullValue_whenCreatingAccountNumber_thenThrowsException() {
      // When & Then
      assertThatThrownBy(() -> new AccountNumber(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("Account number cannot be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Does not allow creating AccountNumber with a blank value")
    void givenBlankValue_whenCreatingAccountNumber_thenThrowsException(String blankValue) {
      // When & Then
      assertThatThrownBy(() -> new AccountNumber(blankValue))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Account number cannot be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "002191123456789012345"})
    @DisplayName("Does not allow creating AccountNumber with invalid length (not 20 digits)")
    void givenInvalidLength_whenCreatingAccountNumber_thenThrowsException(String invalidLength) {
      // When & Then
      assertThatThrownBy(() -> new AccountNumber(invalidLength))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Account number must be exactly 20 digits");
    }

    @Test
    @DisplayName("Does not allow creating AccountNumber with non-numeric characters")
    void givenNonNumericValue_whenCreatingAccountNumber_thenThrowsException() {
      // Given
      var nonNumeric = "002191123456789012AA";

      // When & Then
      assertThatThrownBy(() -> new AccountNumber(nonNumeric))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Account number must contain only digits");
    }
  }

  @Nested
  @DisplayName("Equality and Identity (Value Object Rules)")
  class Equality {

    @Test
    @DisplayName("Two AccountNumber objects with the same value are equal")
    void givenSameValue_whenCompared_thenAreEquals() {
      // Given
      var accountA = new AccountNumber("00219112345678901206");
      var accountB = new AccountNumber("00219112345678901206");

      // When & Then
      assertThat(accountA).isEqualTo(accountB);
    }

    @Test
    @DisplayName("Two AccountNumber objects with the same value have identical hash codes")
    void givenSameValue_whenCheckingHashCode_thenHashCodesMatch() {
      // Given
      var accountA = new AccountNumber("00219112345678901206");
      var accountB = new AccountNumber("00219112345678901206");

      // When & Then
      assertThat(accountA.hashCode()).isEqualTo(accountB.hashCode());
    }

    @Test
    @DisplayName("Two AccountNumber objects with different values are not equal")
    void givenDifferentValues_whenCompared_thenAreNotEquals() {
      // Given
      var accountA = new AccountNumber("00219112345678901206");
      var accountB = new AccountNumber("98765432101234567819");

      // When & Then
      assertThat(accountA).isNotEqualTo(accountB);
    }
  }
}
