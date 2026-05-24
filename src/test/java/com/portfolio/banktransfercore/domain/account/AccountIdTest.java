package com.portfolio.banktransfercore.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AccountIdTest {

    @Test
    @DisplayName("Given valid string, when created, then successful")
    void givenValidString_whenConstructing_thenCreatesSuccessfully() {
        // Given
        String rawId = "ACC-123";

        // When
        AccountId accountId = new AccountId(rawId);

        // Then
        assertThat(accountId.value()).isEqualTo(rawId);
    }

    @Test
    @DisplayName("Given null value, when created, then throws exception")
    void givenNullValue_whenConstructing_thenThrowsNullPointerException() {
        // Given
        String rawId = null;

        // When / Then
        assertThatThrownBy(() -> new AccountId(rawId))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("AccountId cannot be null");
    }

    @Test
    @DisplayName("Given blank value, when created, then throws exception")
    void givenBlankValue_whenConstructing_thenThrowsIllegalArgumentException() {
        // Given
        String rawId = "   ";

        // When / Then
        assertThatThrownBy(() -> new AccountId(rawId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("AccountId cannot be blank");
    }

    @Test
    @DisplayName("Given two equal values, when compared, then they are equivalent")
    void givenTwoEqualValues_whenCompared_thenTheyAreEquivalent() {
        // Given
        AccountId id1 = new AccountId("ID-1");
        AccountId id2 = new AccountId("ID-1");

        // When
        boolean areEqual = id1.equals(id2);

        // Then
        assertThat(areEqual).isTrue();
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }
}