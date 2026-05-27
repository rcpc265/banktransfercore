package com.portfolio.banktransfercore.domain.account;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountIdTest {

  @Nested
  @DisplayName("Creation and Validation Rules")
  class CreationAndValidationRules {

    @Test
    @DisplayName("Does not allow creating AccountId with a null value")
    void givenNullValue_whenCreatingAccountId_thenThrowsException() {
      // When & Then
      assertThatThrownBy(() -> new AccountId(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("AccountId cannot be null");
    }

    @Test
    @DisplayName("Successfully creates AccountId with a valid UUID")
    void givenValidUUID_whenConstructing_thenCreatesSuccessfully() {
      // Given
      var rawId = UUID.randomUUID();

      // When
      var accountId = new AccountId(rawId);

      // Then
      assertThat(accountId.value()).isEqualTo(rawId);
    }
  }

  @Nested
  @DisplayName("Equality and Identity (Value Object Rules)")
  class Equality {

    @Test
    @DisplayName("Two AccountId objects with the same UUID are equal")
    void givenTwoEqualValues_whenCompared_thenAreEquals() {
      // Given
      var uuid = UUID.randomUUID();
      var id1 = new AccountId(uuid);
      var id2 = new AccountId(uuid);

      // When & Then
      assertThat(id1).isEqualTo(id2);
    }

    @Test
    @DisplayName("Two AccountId objects with the same UUID have identical hash codes")
    void givenSameValue_whenCheckingHashCode_thenHashCodesMatch() {
      // Given
      var uuid = UUID.randomUUID();
      var id1 = new AccountId(uuid);
      var id2 = new AccountId(uuid);

      // When & Then
      assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("Two AccountId objects with different UUIDs are not equal")
    void givenDifferentValues_whenCompared_thenAreNotEquals() {
      // Given
      var id1 = new AccountId(UUID.randomUUID());
      var id2 = new AccountId(UUID.randomUUID());

      // When & Then
      assertThat(id1).isNotEqualTo(id2);
    }
  }
}
